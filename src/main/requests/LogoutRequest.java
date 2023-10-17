package requests;

import models.AuthToken;

public class LogoutRequest {

    /**
     * the user's authentication token for the current session
     */
    private AuthToken authToken;

    /**
     * Create a new request to log out of the current session
     * @param authToken the user's authentication token for the current session
     */
    public LogoutRequest(AuthToken authToken) {

    }

    public AuthToken getAuthToken() {
        return null;
    }

}
