package responses;

import chess.ChessGame;
import models.Game;

import java.util.List;
import java.util.Set;

public class ListGamesResponse {

    /**
     * a class internal to this type of request. The purpose of this class is to represent key information
     * about a game without needing to represent the game itself
     */
    private class gameRepresentation {
        /**
         * the id # of the game being represented
         */
        private int gameID;
        /**
         * the username of the user playing as white in this game (may be blank)
         */
        private String whiteUsername;
        /**
         * the username of the user playing as black in this game (may be blank)
         */
        private String blackUsername;
        /**
         * the name of the game being represented
         */
        private String gameName;

        /**
         * given a game from the database, create a simplified representation that can be listed
         * @param game the game to be represented
         */
        public gameRepresentation(Game game) {

        }
    }

    /**
     * a list of simplified representations of all the games in the database
     */
    private List<gameRepresentation> games;
    /**
     * a message indicating why the request to list all the games was unsuccessful
     */
    private String message;

    /**
     * create a new response to provide a list of all games in the database
     * @param games all the games in the database
     */
    public ListGamesResponse(Set<Game> games) {

    }

    /**
     * create a new response to indicate that the request to list all games was unsuccessful
     * @param message a message indicating why the request to list all the games was unsuccessful
     */
    public ListGamesResponse(String message) {

    }

    public String getMessage() {
        return null;
    }

    public List<gameRepresentation> getGamesList() {
        return null;
    }
}
