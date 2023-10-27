package models;

public class AuthToken {

    /**
     * the username of the user to whom the authentication token belongs
     */
    private String username;
    /**
     * a unique code representing the user's authentication for the current session
     */
    private String authToken;

    /**
     * Create a new authentication token that the user can use during the session to make requests that require
     * authentication
     * @param username the username of the user to whom the authentication token belongs
     * @param authToken a unique code representing the user's authentication for the current session
     */
    public AuthToken(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

}
