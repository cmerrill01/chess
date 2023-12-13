package ui;

import chess.*;
import models.Game;
import responses.*;
import server.ResponseException;
import server.ServerFacade;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.util.Arrays;

public class ChessClient {

    private final ServerFacade facade;
    private WebSocketFacade ws;
    private final NotificationHandler notificationHandler;
    private final String serverUrl;
    private ClientState state = ClientState.LOGGEDOUT;
    private String authToken;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        facade = new ServerFacade(serverUrl);
        this.notificationHandler = notificationHandler;
        this.serverUrl = serverUrl;
    }

    public String evaluate(String input) {
        var tokens = input.toLowerCase().split(" ");
        var command = (tokens.length > 0) ? tokens[0] : "help";
        var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
        try {
            return switch (command) {
                case "quit" -> quit();
                case "login" -> login(parameters);
                case "register" -> register(parameters);
                case "logout" -> logout();
                case "create" -> createGame(parameters);
                case "list" -> listGames();
                case "join" -> joinGame(parameters);
                case "observe" -> observeGame(parameters);
                case "redraw" -> redrawBoard();
                case "leave" -> leave();
                case "move" -> makeMove(parameters);
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves();
                default -> help();
            };
        } catch (ResponseException e) {
            return e.getMessage();
        }

    }

    public String login(String... params) throws ResponseException {
        assertLoggedOut();
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];
            LoginResponse response = facade.login(username, password);
            if (response.getAuthToken() != null && response.getUsername() != null) {
                authToken = response.getAuthToken();
                state = ClientState.LOGGEDIN;
                return String.format("Successfully logged in as %s.", response.getUsername());
            } else {
                return String.format("Error: %s", response.getMessage());
            }
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String register(String... params) throws ResponseException {
        assertLoggedOut();
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
        if (state == ClientState.WHITE || state == ClientState.BLACK || state == ClientState.OBSERVER) leave();
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
        assertNotInGame();
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
        assertNotInGame();
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
        assertNotInGame();
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
                ws = new WebSocketFacade(serverUrl, notificationHandler, authToken);
                ws.joinPlayer(gameId, teamColor);
                switch (teamColor) {
                    case WHITE -> state = ClientState.WHITE;
                    case BLACK -> state = ClientState.BLACK;
                }
                return String.format("Successfully joined game with id: %d as the %s player.",
                        gameId, teamColor.toString().toLowerCase());
            } else {
                return String.format("Error: %s", response.getMessage());
            }
        }
        throw new ResponseException(400, "Expected: <game_id> <WHITE|BLACK>");
    }

    public String observeGame(String... params) throws ResponseException {
        assertLoggedIn();
        assertNotInGame();
        if (params.length == 1) {
            int gameId = Integer.parseInt(params[0]);
            JoinGameResponse response = facade.joinGame(authToken, null, gameId);
            if (response.getMessage() == null) {
                ws = new WebSocketFacade(serverUrl, notificationHandler, authToken);
                ws.joinObserver(gameId);
                state = ClientState.OBSERVER;
                return String.format("Successfully joined game with id: %d as an observer.", gameId);
            } else {
                return String.format("Error: %s", response.getMessage());
            }
        }
        throw new ResponseException(400, "Expected: <game_id>");
    }

    public String redrawBoard() {
        return "TODO: finish implementation of redrawBoard\n";
    }

    public String leave() throws ResponseException {
        assertInGame();
        ws.leave();
        ws = null;
        state = ClientState.LOGGEDIN;
        return "Successfully left the game.";
    }

    public String makeMove(String ... params) throws ResponseException {
        assertPlayer();
        if (params.length == 2) {
            int startCol = 8 - (params[0].toCharArray()[0] - 'a');
            int startRow = params[0].toCharArray()[1] - '0';
            ChessPosition startPosition = new ChessPositionImpl(startRow, startCol);
            int toCol = 8 - (params[1].toCharArray()[0] - 'a');
            int toRow = params[1].toCharArray()[1] - '0';
            ChessPosition toPosition = new ChessPositionImpl(toRow, toCol);
            ChessMove move = new ChessMoveImpl(startPosition, toPosition);
            ws.makeMove(move);
            return String.format("Successfully made move: %s %s", params[0], params[1]);
        }
        throw new ResponseException(400, "Expected: <start_position> <end_position> (e.g., a2 a3)");
    }

    public String resign() throws ResponseException {
        assertInGame();
        ws.resign();
        return "Successfully resigned.";
    }

    public String highlightLegalMoves() {
        return "TODO: finish implementation of highlightLegalMoves\n";
    }

    public String help() {
        if (state == ClientState.LOGGEDOUT) {
            return """
                - help
                - quit
                - login <username> <password>
                - register <username> <password> <email>""";
        } else if (state == ClientState.LOGGEDIN) {
            return """
                - help
                - logout
                - create <game_name>
                - list
                - join <game_id> <WHITE|BLACK>
                - observe <game_id>""";
        } else if (state == ClientState.OBSERVER) {
            return """
                - help
                - redraw (Redraws the chess board)
                - leave
                - highlight <piece_position> (e.g., a2) (Highlights legal moves)""";
        }
        return """
                - help
                - redraw (Redraws the chess board)
                - leave
                - move <start_position> <end_position> (e.g., a2 a3)
                - resign
                - highlight <piece_position> (e.g., a2) (Highlights legal moves)""";
    }

    public String quit() throws ResponseException {
        if (state == ClientState.WHITE || state == ClientState.BLACK || state == ClientState.OBSERVER) leave();
        if (state != ClientState.LOGGEDOUT) logout();
        return "quit";
    }

    private void assertLoggedIn() throws ResponseException {
        if (state == ClientState.LOGGEDOUT) {
            throw new ResponseException(400, "Please log in before performing this action.");
        }
    }

    private void assertLoggedOut() throws ResponseException {
        if (state != ClientState.LOGGEDOUT) {
            throw new ResponseException(400, "Please log out before performing this action.");
        }
    }

    private void assertInGame() throws ResponseException {
        if (!(state == ClientState.WHITE || state == ClientState.BLACK || state == ClientState.OBSERVER)) {
            throw new ResponseException(400, "Please join a game before performing this action.");
        }
    }

    private void assertNotInGame() throws ResponseException {
        if (state == ClientState.WHITE || state == ClientState.BLACK || state == ClientState.OBSERVER) {
            throw new ResponseException(400, "Please leave the game before performing this action.");
        }
    }

    private void assertPlayer() throws ResponseException {
        if (!(state == ClientState.WHITE || state == ClientState.BLACK)) {
            throw new ResponseException(400, "Please join a game as a player before performing this action.");
        }
    }

    public ClientState getState() {
        return state;
    }
}
