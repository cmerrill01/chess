package services;

import daos.GameDAO;
import daos.memoryDatabase;
import dataAccess.DataAccessException;
import requests.JoinGameRequest;
import responses.JoinGameResponse;

public class JoinGameService {

    /**
     * Execute a user's request to join a game as a player or an observer
     * @param request contains the id of the game to be joined and the color that the user wishes to play as
     *                (if applicable)
     * @return a response indicating whether the user has successfully joined the game
     */
    public JoinGameResponse joinGame(JoinGameRequest request, memoryDatabase db) {

        JoinGameResponse response;

        GameDAO gameDAO = new GameDAO(db.getGameTable());

        try {
            if (gameDAO.findGame(request.getGameID()) == null) response = new JoinGameResponse("Error: bad request");
            else {
                gameDAO.claimSpotInGame(request.getUsername(), request.getGameID(), request.getPlayerColor());
                response = new JoinGameResponse();
            }
        } catch (DataAccessException e) {
            response = new JoinGameResponse(e.getMessage());
        }

        return response;
    }

}
