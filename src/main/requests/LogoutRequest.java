package requests;

public class LogoutRequest {

    /**
     * the user's authentication token for the current session
     */
    private final String authToken;

    /**
     * Create a new request to log out of the current session
     * @param authToken the user's authentication token for the current session
     */
    public LogoutRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

}
