package requests;

import chess.ChessGame;
import models.AuthToken;

public class JoinGameRequest {

    private AuthToken authToken;
    private ChessGame.TeamColor playerColor;
    private int gameID;

    public JoinGameRequest() {

    }

    public AuthToken getAuthToken() {
        return null;
    }

    public void setAuthToken(AuthToken authToken) {

    }

    public ChessGame.TeamColor getPlayerColor() {
        return null;
    }

    public void setPlayerColor(ChessGame.TeamColor playerColor) {

    }

    public int getGameID() {
        return 0;
    }

    public void setGameID(int gameID) {

    }
}
