package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import daos.AuthDAO;
import daos.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
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
            var command = new Gson().fromJson(message, MakeMoveCommand.class);
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
            if (gameAccess.findGame(gameID) == null) {
                ErrorMessage messageToRoot = new ErrorMessage("Error: The game the user requested to join does not exist.");
                session.getRemote().sendString(new Gson().toJson(messageToRoot));
                return;
            }

            if (authAccess.findAuthToken(authToken) == null) {
                ErrorMessage messageToRoot = new ErrorMessage("Error: The user does not have a valid authentication token.");
                session.getRemote().sendString(new Gson().toJson(messageToRoot));
                return;
            }

            if (playerColor == ChessGame.TeamColor.WHITE) {
                if (!Objects.equals(gameAccess.findGame(gameID).getWhiteUsername(), authAccess.findAuthToken(authToken).username())) {
                    ErrorMessage messageToRoot = new ErrorMessage("Error: The white spot has not been reserved for this player.");
                    session.getRemote().sendString(new Gson().toJson(messageToRoot));
                    return;
                }
            } else if (playerColor == ChessGame.TeamColor.BLACK) {
                if (!Objects.equals(gameAccess.findGame(gameID).getBlackUsername(), authAccess.findAuthToken(authToken).username())) {
                    ErrorMessage messageToRoot = new ErrorMessage("Error: The black spot has not been reserved for this player.");
                    session.getRemote().sendString(new Gson().toJson(messageToRoot));
                    return;
                }
            }
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
            return;
        }

        try {
            connections.add(authToken, gameID, playerColor, session);
        } catch (IOException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
            return;
        }

        try {
            ChessGame game = gameAccess.findGame(gameID).getGame();
            LoadGameMessage messageToRoot = new LoadGameMessage(game);
            session.getRemote().sendString(new Gson().toJson(messageToRoot));

            String username = authAccess.findAuthToken(authToken).username();
            String othersMessageString = String.format("%s joined the game as the %s player.", username, playerColor.toString().toLowerCase());
            NotificationMessage messageToOthers = new NotificationMessage(othersMessageString);
            connections.broadcast(authToken, gameID, messageToOthers);
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
        }
    }

    private void joinObserver(String authToken, int gameID, Session session) {

    }

    private void makeMove(String authToken, int gameID, ChessMove move) {

    }

    private void leave(String authToken, int gameID) {

    }

    private void resign(String authToken, int gameID) {

    }

}
