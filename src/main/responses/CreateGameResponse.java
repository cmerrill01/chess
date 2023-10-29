package responses;

import requests.CreateGameRequest;

public class CreateGameResponse {

    /**
     * the id # for the game that was successfully added to the database
     */
    private int gameID;
    /**
     * a message indicating why the game was not successfully added to the database
     */
    private String message;

    /**
     * Create a new response indicating that the game was successfully added to the database and providing
     * the user with the id # for the new game
     * @param gameID the id # for the game that was successfully added to the database
     */
    public CreateGameResponse(int gameID) {
        this.gameID = gameID;
    }

    /**
     * Create a new response indicating that the game was not successfully added to the database
     * @param message a message indicating why the game was not successfully added to the database
     */
    public CreateGameResponse(String message) {
        this.message = message;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    public String getMessage() {
        return message;
    }

}
