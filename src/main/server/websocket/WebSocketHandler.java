package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daos.AuthDAO;
import daos.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import deserializers.ChessMoveAdapter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Database db;

    public WebSocketHandler(Database database) {
        db = database;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        if (message.toLowerCase().contains("join_player")) {
            var command = new Gson().fromJson(message, JoinPlayerCommand.class);
            joinPlayer(command.getAuthString(), command.getGameID(), command.getPlayerColor(), session);
        } else if (message.toLowerCase().contains("join_observer")) {
            var command = new Gson().fromJson(message, JoinObserverCommand.class);
            joinObserver(command.getAuthString(), command.getGameID(), session);
        } else if (message.toLowerCase().contains("make_move")) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessMove.class, new ChessMoveAdapter());
            var command = builder.create().fromJson(message, MakeMoveCommand.class);
            makeMove(command.getAuthString(), command.getGameID(), command.getMove());
        } else if (message.toLowerCase().contains("leave")) {
            var command = new Gson().fromJson(message, LeaveCommand.class);
            leave(command.getAuthString(), command.getGameID());
        } else if (message.toLowerCase().contains("resign")) {
            var command = new Gson().fromJson(message, ResignCommand.class);
            resign(command.getAuthString(), command.getGameID());
        }
    }

    private void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) throws IOException {
        GameDAO gameAccess = new GameDAO(db);
        AuthDAO authAccess = new AuthDAO(db);

        try {
            connections.add(authToken, gameID, playerColor, session);
        } catch (IOException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
            return;
        }

        try {
            if (invalidGameId(authToken, gameID, gameAccess)) return;
            if (invalidAuthToken(authToken, authAccess)) return;
            if (spotNotReserved(authToken, gameID, playerColor, gameAccess, authAccess)) return;
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
            return;
        }

        try {
            ChessGame game = gameAccess.findGame(gameID).getGame();
            LoadGameMessage messageToRoot = new LoadGameMessage(game);
            connections.send(authToken, messageToRoot);

            String username = authAccess.findAuthToken(authToken).username();
            String othersMessageString = String.format("%s joined the game as the %s player.", username, playerColor.toString().toLowerCase());
            NotificationMessage messageToOthers = new NotificationMessage(othersMessageString);
            connections.broadcast(authToken, gameID, messageToOthers);
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
        }
    }

    private void joinObserver(String authToken, int gameID, Session session) throws IOException {
        GameDAO gameAccess = new GameDAO(db);
        AuthDAO authAccess = new AuthDAO(db);

        try {
            connections.add(authToken, gameID, null, session);
        } catch (IOException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
            return;
        }

        try {
            if (invalidGameId(authToken, gameID, gameAccess)) return;
            if (invalidAuthToken(authToken, authAccess)) return;
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
            return;
        }

        try {
            ChessGame game = gameAccess.findGame(gameID).getGame();
            LoadGameMessage messageToRoot = new LoadGameMessage(game);
            connections.send(authToken, messageToRoot);

            String username = authAccess.findAuthToken(authToken).username();
            String othersMessageString = String.format("%s joined the game as an observer.", username);
            NotificationMessage messageToOthers = new NotificationMessage(othersMessageString);
            connections.broadcast(authToken, gameID, messageToOthers);
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
        }

    }

    private void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        GameDAO gameAccess = new GameDAO(db);
        AuthDAO authAccess = new AuthDAO(db);

        try {
            if (invalidGameId(authToken, gameID, gameAccess)) return;
            if (invalidAuthToken(authToken, authAccess)) return;
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
            return;
        }

        try {
            // Get the game
            ChessGame game = gameAccess.findGame(gameID).getGame();
            // Make the move
            game.makeMove(move);
            // Update the game
            gameAccess.updateGame(gameID, game);
            // Load the board for all players
            LoadGameMessage messageToAll = new LoadGameMessage(game);
            connections.broadcast(null, gameID, messageToAll);
            // Notify other players that a move was made
            NotificationMessage messageToOthers = new NotificationMessage(String.format(
                    "%s has made a move: %s", authAccess.findAuthToken(authToken).username(), move.toString())
            );
            connections.broadcast(authToken, gameID, messageToOthers);
        } catch (DataAccessException | InvalidMoveException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
        }
    }
    private void leave(String authToken, int gameID) {

    }

    private void resign(String authToken, int gameID) {

    }

    private boolean spotNotReserved(String authToken, int gameID, ChessGame.TeamColor playerColor, GameDAO gameAccess, AuthDAO authAccess) throws DataAccessException, IOException {
        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (!Objects.equals(gameAccess.findGame(gameID).getWhiteUsername(), authAccess.findAuthToken(authToken).username())) {
                ErrorMessage messageToRoot = new ErrorMessage("Error: The white spot has not been reserved for this player.");
                connections.send(authToken, messageToRoot);
                return true;
            }
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            if (!Objects.equals(gameAccess.findGame(gameID).getBlackUsername(), authAccess.findAuthToken(authToken).username())) {
                ErrorMessage messageToRoot = new ErrorMessage("Error: The black spot has not been reserved for this player.");
                connections.send(authToken, messageToRoot);
                return true;
            }
        }
        return false;
    }

    private boolean invalidAuthToken(String authToken, AuthDAO authAccess) throws DataAccessException, IOException {
        if (authAccess.findAuthToken(authToken) == null) {
            ErrorMessage messageToRoot = new ErrorMessage("Error: The user does not have a valid authentication token.");
            connections.send(authToken, messageToRoot);
            return true;
        }
        return false;
    }

    private boolean invalidGameId(String authToken, int gameID, GameDAO gameAccess) throws DataAccessException, IOException {
        if (gameAccess.findGame(gameID) == null) {
            ErrorMessage messageToRoot = new ErrorMessage("Error: The game the user requested to join does not exist.");
            connections.send(authToken, messageToRoot);
            return true;
        }
        return false;
    }

}
