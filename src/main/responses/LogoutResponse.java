package responses;

public class LogoutResponse {

    /**
     * a message indicating why the attempt to logout was unsuccessful
     */
    private String message;

    /**
     * Create a new response indicating that the user was successfully logged out of the session
     */
    public LogoutResponse() {

    }

    /**
     * Create a new response indicating that the user was not successfully logged out of the session
     * @param message a message indicating why the attempt to logout was unsuccessful
     */
    public LogoutResponse(String message) {

    }

    public String getMessage() {
        return null;
    }

}
