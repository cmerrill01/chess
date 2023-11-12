package responses;

import models.Game;
import java.util.Set;
import java.util.TreeSet;

public class ListGamesResponse {

    /**
     * a list of simplified representations of all the games in the database
     */
    private Set<Game> games;
    /**
     * a message indicating why the request to list all the games was unsuccessful
     */
    private String message;

    /**
     * create a new response to provide a list of all games in the database
     * @param games all the games in the database
     */
    public ListGamesResponse(Set<Game> games) {
        this.games = new TreeSet<>();
        this.games.addAll(games);
    }

    /**
     * create a new response to indicate that the request to list all games was unsuccessful
     * @param message a message indicating why the request to list all the games was unsuccessful
     */
    public ListGamesResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Set<Game> getGamesList() {
        return games;
    }
}
