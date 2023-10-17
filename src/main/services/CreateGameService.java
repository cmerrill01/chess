package services;

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
    public CreateGameResponse createGame(CreateGameRequest request) {
        return null;
    }

}
