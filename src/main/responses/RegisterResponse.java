package responses;

public class RegisterResponse {

    /**
     * the username assigned to the new user, if the account was created successfully
     */
    private String username;
    /**
     * the string representation of the authentication token provided for the new user's current login session
     */
    private String authToken;
    /**
     * an error message indicating why the new user's account was unable to be added to the database
     */
    private String message;

    /**
     * Create a new response to indicate the successful addition of a new user account to the database
     * @param username the username of the new user
     * @param authToken the string representation of the authentication token provided for the new user's current
     *                  login session
     */
    public RegisterResponse(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    /**
     * Create a new response to indicate the failure to add a new user account to the database
     * @param message a message indicating the reason why account creation was unsuccessful
     */
    public RegisterResponse(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthToken() {
        return authToken;
    }

}
