package services;

import daos.AuthDAO;
import daos.UserDAO;
import dataAccess.DataAccessException;
import models.AuthToken;
import models.User;
import requests.LoginRequest;
import responses.LoginResponse;

import java.util.Objects;
import java.util.UUID;

public class LoginService {

    /**
     * Execute a user's request to log into their account
     * @param request contains the user's credentials (username and password) to log in
     * @return a response indicating whether login was successful and providing the user with an authentication
     * token
     */
    public LoginResponse login(LoginRequest request) {

        LoginResponse response;
        User user;

        UserDAO userDAO = new UserDAO();
        AuthDAO authDAO = new AuthDAO();

        try {
            user = userDAO.findUser(request.getUsername());
            if (Objects.equals(user.getPassword(), request.getPassword())) {
                String authToken = UUID.randomUUID().toString();
                authDAO.insertAuthToken(new AuthToken(user.getUsername(), authToken));
                response = new LoginResponse(user.getUsername(), authToken);
            } else {
                response = new LoginResponse("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            response = new LoginResponse("Error: unauthorized");
        }

        return response;
    }

}
