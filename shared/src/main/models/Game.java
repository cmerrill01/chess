package models;

import chess.ChessGame;
import chess.ChessGameImpl;

import java.util.Objects;

public class Game implements Comparable {

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
    private final String gameName;
    /**
     * the data and functionality of the chess game
     */
    private ChessGame game;

    /**
     * Create a new game (not yet added to the database)
     * @param gameName a nickname for the game, assigned to the game by the user upon creation
     */
    public Game(String gameName) {
        this.gameName = gameName;
        game = new ChessGameImpl();
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game1 = (Game) o;
        return gameID == game1.gameID && Objects.equals(whiteUsername, game1.whiteUsername) && Objects.equals(blackUsername, game1.blackUsername) && Objects.equals(gameName, game1.gameName) && Objects.equals(game, game1.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public int compareTo(Object o) {
        int compare;
        if (o instanceof Game other) {
            compare = this.gameID - other.gameID;
        } else {
            compare = this.hashCode() - o.hashCode();
        }
        return compare;
    }
}