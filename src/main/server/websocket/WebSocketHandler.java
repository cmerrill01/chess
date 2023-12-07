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
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;

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
        connections.add(authToken, gameID, playerColor, session);
        try {
            ChessGame game = new GameDAO(db).findGame(gameID).getGame();
            LoadGameMessage messageToRoot = new LoadGameMessage(game);
            session.getRemote().sendString(new Gson().toJson(messageToRoot));

            String username = new AuthDAO(db).findAuthToken(authToken).username();
            String othersMessageString = String.format("%s joined the game as the %s player.", username, playerColor.toString().toLowerCase());
            NotificationMessage messageToOthers = new NotificationMessage(othersMessageString);
            connections.broadcast(authToken, gameID, messageToOthers);
        } catch (DataAccessException e) {
            session.getRemote().sendString(e.getMessage());
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
