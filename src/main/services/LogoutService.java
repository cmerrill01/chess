package services;

import daos.AuthDAO;
import daos.memoryDatabase;
import dataAccess.DataAccessException;
import requests.LogoutRequest;
import responses.LogoutResponse;

public class LogoutService {

    /**
     * Execute a user's request to log out of their account
     * @param request contains the authentication token for the user's current session
     * @return a response indicating whether logout was successful
     */
    public LogoutResponse logout(LogoutRequest request, memoryDatabase db) {

        LogoutResponse response;

        AuthDAO dao = new AuthDAO(db.getAuthTokenTable());

        try {
            if (dao.findAuthToken(request.getAuthToken()) != null) {
                dao.removeAuthToken(request.getAuthToken());
                response = new LogoutResponse();
            } else {
                response = new LogoutResponse("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            response = new LogoutResponse(e.getMessage());
        }

        return response;
    }

}
