package services;

import requests.ListGamesRequest;
import responses.ListGamesResponse;

public class ListGamesService {

    /**
     * Execute a user's request to list information about all the games in the database
     * @param request contains the user's authentication token for the current session
     * @return a response indicating whether the request was successful and, if so, providing the requested
     * list of games
     */
    public ListGamesResponse listGames(ListGamesRequest request) {
        return null;
    }

}
