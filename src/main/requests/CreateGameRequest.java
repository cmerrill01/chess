package requests;

import models.AuthToken;

public class CreateGameRequest {

    /**
     * the user's authentication token for the current session
     */
    private AuthToken authToken;
    /**
     * the name of the game to be created
     */
    private String gameName;

    /**
     * Create a new request to add a new game to the database
     * @param authToken the user's authentication token for the current session
     * @param gameName the name of the game to be created
     */
    public CreateGameRequest(AuthToken authToken, String gameName) {

    }

    public AuthToken getAuthToken() {
        return null;
    }

    public String getGameName() {
        return null;
    }

}
