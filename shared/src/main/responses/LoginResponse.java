package responses;

public class LoginResponse {

    /**
     * the username of the user who successfully logged in
     */
    private String username;
    /**
     * the string representation of the authentication token assigned to this user for the current session
     */
    private String authToken;
    /**
     * a message indicating why the login attempt was unsuccessful
     */
    private String message;

    /**
     * Create a new response indicating that the login attempt was successful and providing an authentication
     * token for the current session
     * @param username the username of the user who successfully logged in
     * @param authToken the string representation of the authentication token assigned to this user for the
     *                  current session
     */
    public LoginResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    /**
     * Create a new response indicating that the login attempt was unsuccessful
     * @param message a message indicating why the login attempt was unsuccessful
     */
    public LoginResponse(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getMessage() {
        return message;
    }

}
