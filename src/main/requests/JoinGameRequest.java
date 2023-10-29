package requests;

import chess.ChessGame;

public class JoinGameRequest {

    /**
     * the color of the team that the user wishes to join as
     */
    private ChessGame.TeamColor playerColor;
    /**
     * the id # of the game the user wishes to join
     */
    private final int gameID;
    /**
     * the username of the user who wishes to join the game
     */
    private String username;

    /**
     * Create a new request to join an existing game as a player
     * @param gameID the id # of the game the user wishes to join
     * @param playerColor the color of the team that the user wishes to join as
     */
    public JoinGameRequest(Integer gameID, ChessGame.TeamColor playerColor) {
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    /**
     * Create a new request to join an existing game as an observer
     * @param gameID the id # of the game the user wishes to join
     */
    public JoinGameRequest(int gameID) {
        this.gameID = gameID;
    }


    public ChessGame.TeamColor getPlayerColor() {
        return playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
