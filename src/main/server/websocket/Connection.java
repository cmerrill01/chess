package server.websocket;

import chess.ChessGame;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public String authToken;
    public Session session;
    private int gameId;
    private ChessGame.TeamColor playerColor;

    public Connection(String authToken, int gameId, Session session) {
        this.authToken = authToken;
        this.gameId = gameId;
        this.session = session;
        playerColor = null;
    }

    public Connection(String authToken, int gameId, ChessGame.TeamColor playerColor, Session session) {
        this.authToken = authToken;
        this.gameId = gameId;
        this.session = session;
        this.playerColor = playerColor;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }

    public int getGameId() {
        return gameId;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }
}
