package requests;

import chess.ChessGame;
import models.AuthToken;

public class JoinGameRequest {

    /**
     * the user's authentication token for the current session
     */
    private AuthToken authToken;
    /**
     * the color of the team that the user wishes to join as
     */
    private ChessGame.TeamColor playerColor;
    /**
     * the id # of the game the user wishes to join
     */
    private int gameID;

    /**
     * Create a new request to join an existing game as a player
     * @param authToken the user's authentication token for the current session
     * @param gameID the id # of the game the user wishes to join
     * @param playerColor the color of the team that the user wishes to join as
     */
    public JoinGameRequest(AuthToken authToken, int gameID, ChessGame.TeamColor playerColor) {

    }

    /**
     * Create a new request to join an existing game as an observer
     * @param authToken the user's authentication token for the current session
     * @param gameID the id # of the game the user wishes to join
     */
    public JoinGameRequest(AuthToken authToken, int gameID) {

    }

    public AuthToken getAuthToken() {
        return null;
    }

    public ChessGame.TeamColor getPlayerColor() {
        return null;
    }

    public int getGameID() {
        return 0;
    }

}
