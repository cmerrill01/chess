package services;

import requests.CreateGameRequest;
import responses.CreateGameResponse;

public class JoinGameService {

    /**
     * Execute a user's request to join a game as a player or an observer
     * @param request contains the user's authentication token for the current session, the id of the game
     *                to be joined, and the color that the user wishes to play as (if applicable)
     * @return a response indicating whether the user has successfully joined the game
     */
    public CreateGameResponse createGame(CreateGameRequest request) {
        return null;
    }

}
