package services;

import daos.*;
import dataAccess.DataAccessException;
import dataAccess.Database;
import responses.ClearApplicationResponse;

public class ClearApplicationService {

    /**
     * Execute a user's request to clear all data from the database
     * @return a response indicating whether all data was successfully cleared
     */
    public ClearApplicationResponse clearApplication(Database database) {

        ClearApplicationResponse response;

        try {
            new AuthDAO(database).clearAuthTokens();
            new UserDAO(database).clearUsers();
            new GameDAO(database).clearGames();
            response = new ClearApplicationResponse();
        } catch (DataAccessException e) {
            response = new ClearApplicationResponse("Error: database not cleared");
        }

        return response;
    }

}
