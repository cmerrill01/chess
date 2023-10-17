package models;

import chess.ChessGame;

public class Game {

    /**
     * a unique identifier #, assigned to the game by the system upon creation
     */
    private int gameID;
    /**
     * the username of the user playing as white
     */
    private String whiteUsername;
    /**
     * the username of the user playing as black
     */
    private String blackUsername;
    /**
     * a nickname for the game, assigned to the game by the user upon creation
     */
    private String gameName;
    /**
     * the data and functionality of the chess game
     */
    private ChessGame game;

    /**
     * Create a new game (not yet added to the database)
     * @param gameName a unique identifier #, assigned to the game by the system upon creation
     * @param gameID a nickname for the game, assigned to the game by the user upon creation
     */
    public Game(String gameName, int gameID) {

    }

    public int getGameID() {
        return 0;
    }

    public String getWhiteUsername() {
        return null;
    }

    public void setWhiteUsername(String whiteUsername) {

    }

    public String getBlackUsername() {
        return null;
    }

    public void setBlackUsername(String blackUsername) {

    }

    public String getGameName() {
        return null;
    }

}
