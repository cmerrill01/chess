package server;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import deserializers.ChessGameAdapter;
import requests.CreateGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ClearApplicationResponse clear() {
        String path = "/db";
        try {
            return this.makeRequest("DELETE", path, null, null, ClearApplicationResponse.class);
        } catch (ResponseException e) {
            return new ClearApplicationResponse(e.getMessage());
        }
    }

    public RegisterResponse register(String username, String password, String email) {
        String path = "/user";
        RegisterRequest request = new RegisterRequest(username, password, email);
        try {
            return makeRequest("POST", path, request, null, RegisterResponse.class);
        } catch (ResponseException e) {
            return new RegisterResponse(e.getMessage());
        }
    }

    public LoginResponse login(String username, String password){
        String path = "/session";
        LoginRequest request = new LoginRequest(username, password);
        try {
            return makeRequest("POST", path, request, null, LoginResponse.class);
        } catch (ResponseException e) {
            return new LoginResponse(e.getMessage());
        }
    }

    public LogoutResponse logout(String authToken) {
        String path = "/session";
        try {
            return makeRequest("DELETE", path, null, authToken, LogoutResponse.class);
        } catch (ResponseException e) {
            return new LogoutResponse(e.getMessage());
        }
    }

    public ListGamesResponse listGames(String authToken) {
        String path = "/game";
        try {
            return makeRequest("GET", path, null, authToken, ListGamesResponse.class);
        } catch (ResponseException e) {
            return new ListGamesResponse(e.getMessage());
        }
    }

    public CreateGameResponse createGame(String authToken, String gameName) {
        String path = "/game";
        CreateGameRequest request = new CreateGameRequest(gameName);
        try {
            return makeRequest("POST", path, request, authToken, CreateGameResponse.class);
        } catch (ResponseException e) {
            return new CreateGameResponse(e.getMessage());
        }
    }

    public JoinGameResponse joinGame(String authToken, ChessGame.TeamColor playerColor, int gameId) {
        return null;
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws ResponseException {

        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeAuthHeader(authToken, http);
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }

    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String requestData = new Gson().toJson(request);
            try (OutputStream requestBody = http.getOutputStream()) {
                requestBody.write(requestData.getBytes());
            }
        }
    }

    private static void writeAuthHeader(String authToken, HttpURLConnection http) throws IOException {
        if (authToken != null) {
            http.addRequestProperty("Authorization", authToken);
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "Failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream responseBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(responseBody);
                if (responseClass != null) {
                    var builder = new GsonBuilder();
                    builder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
                    response = builder.create().fromJson(reader, responseClass);
                    // response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        http.disconnect();
        return response;
    }

}
