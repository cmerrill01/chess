package services;

import daos.AuthDAO;
import daos.GameDAO;
import daos.memoryDatabase;
import dataAccess.DataAccessException;
import models.Game;
import requests.CreateGameRequest;
import responses.CreateGameResponse;

public class CreateGameService {

    /**
     * Execute a user's request to create a new game
     * @param request contains the user's authentication token for the current session and a name for the new
     *                game
     * @return a response indicating whether the game was successfully created and, if so, providing an id #
     * for the new game
     */
    public CreateGameResponse createGame(CreateGameRequest request, memoryDatabase db) {

        if (request.getGameName() == null) return new CreateGameResponse("Error: bad request");

        CreateGameResponse response;

        AuthDAO authDAO = new AuthDAO(db.getAuthTokenTable());
        GameDAO gameDAO = new GameDAO(db.getGameTable());

        try {
            int gameID = gameDAO.insertGame(new Game(request.getGameName()));
            response = new CreateGameResponse(gameID);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

}
