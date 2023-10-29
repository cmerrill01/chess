package requests;

public class ListGamesRequest {

    /**
     * the user's authentication token for the current session
     */
    private final String authToken;

    /**
     * create a request to list a representation of all the games in the database
     * @param authToken the user's authentication token for the current session
     */
    public ListGamesRequest(String authToken) {
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

}
