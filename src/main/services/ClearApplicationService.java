package services;

import daos.*;
import dataAccess.DataAccessException;
import responses.ClearApplicationResponse;

public class ClearApplicationService {

    /**
     * Execute a user's request to clear all data from the database
     * @return a response indicating whether all data was successfully cleared
     */
    public ClearApplicationResponse clearApplication(memoryDatabase database) {

        ClearApplicationResponse response;

        try {
            new AuthDAO(database.getAuthTokenTable()).clearAuthTokens();
            new UserDAO(database.getUserTable()).clearUsers();
            new GameDAO(database.getGameTable()).clearGames();
            response = new ClearApplicationResponse();
        } catch (DataAccessException e) {
            response = new ClearApplicationResponse("Error: database not cleared");
        }

        return response;
    }

}
