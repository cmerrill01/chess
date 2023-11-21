package ui;

import chess.ChessGame;
import models.Game;
import responses.*;
import server.ResponseException;
import server.ServerFacade;

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

    public String logout() throws ResponseException {
        assertLoggedIn();
        LogoutResponse response = facade.logout(authToken);
        if (response.getMessage() == null) {
            state = ClientState.LOGGEDOUT;
            authToken = null;
            return "Successfully logged out.";
        } else {
            return String.format("Error: %s", response.getMessage());
        }
    }

    public String createGame(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 1) {
            String gameName = params[0];
            CreateGameResponse response = facade.createGame(authToken, gameName);
            if (response.getGameID() != null) {
                return String.format("Successfully created game with id: %d", response.getGameID());
            } else {
                return String.format("Error: %s", response.getMessage());
            }
        }
        throw new ResponseException(400, "Expected: <game_name>");
    }

    public String listGames() throws ResponseException {
        assertLoggedIn();
        ListGamesResponse response = facade.listGames(authToken);
        if (response.getMessage() == null) {
            StringBuilder gamesList = new StringBuilder();
            for (Game game : response.getGamesList()) {
                gamesList.append(String.format("%n  Game ID: %d; White Username: %s; Black Username: %s; Game Name: %s",
                        game.getGameID(), game.getWhiteUsername(), game.getBlackUsername(), game.getGameName()));
            }
            return String.format("Games list: %s", gamesList);
        } else {
            return String.format("Error: %s", response.getMessage());
        }
    }

    public String joinGame(String... params) throws ResponseException {
        assertLoggedIn();
        if (params.length == 2) {
            int gameId = Integer.parseInt(params[0]);
            ChessGame.TeamColor teamColor;
            switch (params[1]) {
                case "white" -> teamColor = ChessGame.TeamColor.WHITE;
                case "black" -> teamColor = ChessGame.TeamColor.BLACK;
                default -> throw new ResponseException(400, "Expected: <game_id> <WHITE|BLACK>");
            }
            JoinGameResponse response = facade.joinGame(authToken, teamColor, gameId);
            if (response.getMessage() == null) {
                return String.format("Successfully joined game with id: %d as the %s player.",
                        gameId, teamColor.toString().toLowerCase());
            } else {
                return String.format("Error: %s", response.getMessage());
            }
        }
        throw new ResponseException(400, "Expected: <game_id> <WHITE|BLACK>");
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

    private void assertLoggedIn() throws ResponseException {
        if (state == ClientState.LOGGEDOUT) {
            throw new ResponseException(400, "Please log in.");
        }
    }

}
