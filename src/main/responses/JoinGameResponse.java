package responses;

public class JoinGameResponse {

    /**
     * a message indicating why the request to join an existing game was unsuccessful
     */
    private String message;

    /**
     * Create a new response indicating that the existing game was successfully joined
     */
    public JoinGameResponse() {

    }

    /**
     * Create a new response indicating that the user was unable to join the existing game as requested
     * @param message a message indicating why the request to join an existing game was unsuccessful
     */
    public JoinGameResponse(String message) {

    }

    public String getMessage() {
        return null;
    }

}
