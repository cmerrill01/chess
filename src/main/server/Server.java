package server;

import com.google.gson.Gson;
import daos.AuthDAO;
import daos.memoryDatabase;
import dataAccess.DataAccessException;
import requests.*;
import responses.*;
import services.*;
import spark.*;

import java.util.List;
import java.util.Objects;


public class Server {

    private final memoryDatabase db = new memoryDatabase();

    public static void main(String[] args) {
        new Server().run();
    }

    private void run() {

        Spark.port(8080);

        Spark.externalStaticFileLocation("web");

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("session", this::logout);
        Spark.get("/game",  this::listGames);
        Spark.post("/game", this::createGame);
    }

    private Object clear(Request req, Response res) {
        System.out.println("Received body: " + req.body());
        ClearApplicationService service = new ClearApplicationService();
        ClearApplicationResponse response = service.clearApplication(db);
        return new Gson().toJson(response, ClearApplicationResponse.class);
    }

    private Object register(Request req, Response res) {
        System.out.println("Received body: " + req.body());
        RegisterRequest request = new Gson().fromJson(req.body(), RegisterRequest.class);
        RegisterService service = new RegisterService();
        RegisterResponse response = service.register(request, db);
        if (Objects.equals(response.getMessage(), "Error: bad request")) res.status(400);
        else if (Objects.equals(response.getMessage(), "Error: already taken")) res.status(403);
        return new Gson().toJson(response, RegisterResponse.class);
    }

    private Object login(Request req, Response res) {
        System.out.println("Received body: " + req.body());
        LoginRequest request = new Gson().fromJson(req.body(), LoginRequest.class);
        LoginService service = new LoginService();
        LoginResponse response = service.login(request, db);
        if (Objects.equals(response.getMessage(), "Error: unauthorized")) res.status(401);
        return new Gson().toJson(response, LoginResponse.class);
    }

    private Object logout(Request req, Response res) {
        System.out.println("Received headers: " + req.headers());
        LogoutRequest request = new LogoutRequest(req.headers("Authorization"));
        LogoutService service = new LogoutService();
        LogoutResponse response = service.logout(request, db);
        if (Objects.equals(response.getMessage(), "Error: unauthorized")) res.status(401);
        return new Gson().toJson(response, LogoutResponse.class);
    }

    private Object listGames(Request req, Response res) {
        System.out.println("Received headers: " + req.headers());
        ListGamesRequest request = new ListGamesRequest(req.headers("Authorization"));
        ListGamesService service = new ListGamesService();
        ListGamesResponse response = service.listGames(request, db);
        if (Objects.equals(response.getMessage(), "Error: unauthorized")) res.status(401);
        return new Gson().toJson(response, ListGamesResponse.class);
    }

    private Object createGame(Request req, Response res) {
        System.out.println("Received headers: " + req.headers());
        CreateGameResponse response;
        try {
            if (new AuthDAO(db.getAuthTokenTable()).findAuthToken(req.headers("Authorization")) == null) {
                response = new CreateGameResponse("Error: unauthorized");
                res.status(401);
            } else {
                CreateGameRequest request = new Gson().fromJson(req.body(), CreateGameRequest.class);
                CreateGameService service = new CreateGameService();
                response = service.createGame(request, db);
            }
        } catch (DataAccessException e) {
            response = new CreateGameResponse(e.getMessage());
        }
        return new Gson().toJson(response, CreateGameResponse.class);
    }

}
