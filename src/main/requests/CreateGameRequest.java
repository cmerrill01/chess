package requests;

import models.AuthToken;

public class CreateGameRequest {

    /**
     * the name of the game to be created
     */
    private String gameName;

    /**
     * Create a new request to add a new game to the database
     * @param gameName the name of the game to be created
     */
    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        return gameName;
    }

}
