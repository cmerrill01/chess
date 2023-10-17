package requests;

import models.AuthToken;

public class ListGamesRequest {

    /**
     * the user's authentication token for the current session
     */
    private AuthToken authToken;

    /**
     * create a request to list a representation of all the games in the database
     * @param authToken the user's authentication token for the current session
     */
    public ListGamesRequest(AuthToken authToken) {

    }

    public AuthToken getAuthToken() {
        return null;
    }

}
