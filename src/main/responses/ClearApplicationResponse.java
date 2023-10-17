package responses;

public class ClearApplicationResponse {

    /**
     * a message indicating why the request to remove all data from the database was unsuccessful
     */
    private String message;

    /**
     * Create a new response indicating that all information was successfully removed from the database
     */
    public ClearApplicationResponse() {

    }

    /**
     * Create a new response indicating that all information was not successfully removed from the database
     * @param message a message indicating why the request to remove all data from the database was unsuccessful
     */
    public ClearApplicationResponse(String message) {

    }

    public String getMessage() {
        return null;
    }

}
