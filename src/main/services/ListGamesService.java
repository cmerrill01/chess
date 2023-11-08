package services;

import daos.AuthDAO;
import daos.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import requests.ListGamesRequest;
import responses.ListGamesResponse;

public class ListGamesService {

    /**
     * Execute a user's request to list information about all the games in the database
     * @param request contains the user's authentication token for the current session
     * @return a response indicating whether the request was successful and, if so, providing the requested
     * list of games
     */
    public ListGamesResponse listGames(ListGamesRequest request, Database db) {
        ListGamesResponse response;
        AuthDAO authDAO = new AuthDAO(db);
        GameDAO gameDAO = new GameDAO(db);

        try {
            if (authDAO.findAuthToken(request.getAuthToken()) != null) {
                response = new ListGamesResponse(gameDAO.findAllGames());
            } else {
                response = new ListGamesResponse("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            response = new ListGamesResponse(e.getMessage());
        }

        return response;
    }

}
