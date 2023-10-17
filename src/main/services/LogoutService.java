package services;

import requests.LogoutRequest;
import responses.LoginResponse;
import responses.LogoutResponse;

public class LogoutService {

    /**
     * Execute a user's request to log out of their account
     * @param request contains the authentication token for the user's current session
     * @return a response indicating whether logout was successful
     */
    public LogoutResponse logout(LogoutRequest request) {
        return null;
    }

}
