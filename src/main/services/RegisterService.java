package services;

import daos.AuthDAO;
import daos.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import models.AuthToken;
import models.User;
import requests.RegisterRequest;
import responses.RegisterResponse;

import java.util.UUID;

public class RegisterService {

    /**
     * Execute a user's request to register, i.e., create an account
     * @param request contains the details of the user's username, email, and password
     * @return a response indicating whether the user was successfully added to the database
     */
    public RegisterResponse register(RegisterRequest request, Database db) {

        if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
            return new RegisterResponse("Error: bad request");
        }

        RegisterResponse response;

        User user = new User(request.getUsername(), request.getPassword(), request.getEmail());
        AuthToken token = new AuthToken(request.getUsername(), UUID.randomUUID().toString());
        UserDAO usersAccess = new UserDAO(db);
        AuthDAO tokensAccess = new AuthDAO(db);

        try {
            usersAccess.insertUser(user);
            tokensAccess.insertAuthToken(token);
            response = new RegisterResponse(user.getUsername(), token.authToken());
        } catch (DataAccessException e) {
            response = new RegisterResponse(e.getMessage());
        }

        return response;
    }

}
