package ui;

import responses.LoginResponse;
import responses.RegisterResponse;
import server.ResponseException;
import server.ServerFacade;

import java.lang.module.ResolutionException;
import java.util.Arrays;

public class ChessClient {

    private final ServerFacade facade;
    private ClientState state = ClientState.LOGGEDOUT;
    private String authToken;

    public ChessClient(String serverUrl) {
        facade = new ServerFacade(serverUrl);
    }

    public String evaluate(String input) {
        var tokens = input.toLowerCase().split(" ");
        var command = (tokens.length > 0) ? tokens[0] : "help";
        var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (command) {
                case "quit" -> "quit";
                case "login" -> login(parameters);
                case "register" -> register(parameters);
                case "logout" -> logout();
                case "create" -> createGame(parameters);
                case "list" -> listGames();
                case "join" -> joinGame(parameters);
                case "observe" -> observeGame(parameters);
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }

    }

    public String login(String... params) throws ResponseException {
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            LoginResponse response = facade.login(username, password);
            if (response.getAuthToken() != null && response.getUsername() != null) {
                authToken = response.getAuthToken();
                state = ClientState.LOGGEDIN;
                return String.format("Successfully signed in as %s.", response.getUsername());
            } else {
                return String.format("Error: %s", response.getMessage());
            }
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterResponse response = facade.register(username, password, email);
            if (response.getAuthToken() != null && response.getUsername() != null) {
                authToken = response.getAuthToken();
                state = ClientState.LOGGEDIN;
                return String.format("Successfully registered new user: %s.", response.getUsername());
            } else {
                return String.format("Error: %s", response.getMessage());
            }
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String logout() {
        return null;
    }

    public String createGame(String... params) {
        return null;
    }

    public String listGames() {
        return null;
    }

    public String joinGame(String... params) {
        return null;
    }

    public String observeGame(String... params) {
        return null;
    }

    public String help() {
        if (state == ClientState.LOGGEDOUT) {
            return """
                    - help
                    - quit
                    - login <username> <password>
                    - register <username> <password> <email>
                    """;
        }
        return """
                - help
                - logout
                - create <game_name>
                - list
                - join <game_id> <WHITE|BLACK>
                - observe <game_id>
                """;
    }

}
