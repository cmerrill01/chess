package requests;

import models.AuthToken;

public class CreateGameRequest {

    /**
     * the user's authentication token for the current session
     */
    private String authToken;
    /**
     * the name of the game to be created
     */
    private String gameName;

    /**
     * Create a new request to add a new game to the database
     * @param authToken the user's authentication token for the current session
     * @param gameName the name of the game to be created
     */
    public CreateGameRequest(String authToken, String gameName) {
        this.authToken = authToken;
        this.gameName = gameName;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getGameName() {
        return gameName;
    }

}
