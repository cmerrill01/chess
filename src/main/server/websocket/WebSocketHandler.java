package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;

public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        if (message.toLowerCase().contains("join_player")) {
            var command = new Gson().fromJson(message, JoinPlayerCommand.class);
            joinPlayer(command.getAuthString(), command.getGameId(), command.getPlayerColor());
        } else if (message.toLowerCase().contains("join_observer")) {
            var command = new Gson().fromJson(message, JoinObserverCommand.class);
            joinObserver(command.getAuthString(), command.getGameId());
        } else if (message.toLowerCase().contains("make_move")) {
            var command = new Gson().fromJson(message, MakeMoveCommand.class);
            makeMove(command.getAuthString(), command.getGameId(), command.getMove());
        } else if (message.toLowerCase().contains("leave")) {
            var command = new Gson().fromJson(message, LeaveCommand.class);
            leave(command.getAuthString(), command.getGameId());
        } else if (message.toLowerCase().contains("resign")) {
            var command = new Gson().fromJson(message, ResignCommand.class);
            resign(command.getAuthString(), command.getGameId());
        }
    }

    private void joinPlayer(String authToken, int gameId, ChessGame.TeamColor playerColor) {

    }

    private void joinObserver(String authToken, int gameId) {

    }

    private void makeMove(String authToken, int gameId, ChessMove move) {

    }

    private void leave(String authToken, int gameId) {

    }

    private void resign(String authToken, int gameId) {

    }

}
