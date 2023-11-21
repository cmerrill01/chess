import chess.ChessGame;
import models.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responses.*;
import server.ServerFacade;

import java.util.Set;


public class ClientTests {

    private static ServerFacade facade;
    private static final String serverUrl = "http://localhost:8080";
    private static final String testUsername = "john";
    private static final String testPassword = "sltw316";
    private static final String testEmail = "beloved@patmos.com";
    private static final String testGameName_1 = "gospel";
    private static final String testGameName_2 = "revelation";

    @BeforeEach
    public void initializeAndClear() {
        facade = new ServerFacade(serverUrl);
        facade.clear();
    }

    @Test
    public void clearSuccess() {
        ClearApplicationResponse response = facade.clear();
        Assertions.assertNull(response.getMessage(), "Success response contains a message");
    }

    @Test
    public void registerSuccess() {
        RegisterResponse response = facade.register(testUsername, testPassword, testEmail);
        Assertions.assertEquals(testUsername, response.getUsername(), "Success response does not contain the user's username");
        Assertions.assertNotNull(response.getAuthToken(), "Success response does not contain an authToken");
        Assertions.assertNull(response.getMessage(), "Success response contains a message");
    }

    @Test
    public void registerFail() {
        facade.register(testUsername, testPassword, testEmail);
        RegisterResponse response = facade.register(testUsername, testPassword, testEmail);
        Assertions.assertNull(response.getUsername(), "Failure response contains a username");
        Assertions.assertNull(response.getAuthToken(), "Failure response contains an authToken");
        Assertions.assertNotNull(response.getMessage(), "Failure response does not contain a message");
    }

    @Test
    public void loginSuccess() {
        facade.register(testUsername, testPassword, testEmail);
        LoginResponse response = facade.login(testUsername, testPassword);
        Assertions.assertEquals(testUsername, response.getUsername(), "Success response does not contain the correct username");
        Assertions.assertNotNull(response.getAuthToken(), "Success response does not contain an authToken");
        Assertions.assertNull(response.getMessage(), "Success response contains a message");
    }

    @Test
    public void loginFail() {
        LoginResponse response = facade.login(testUsername, testPassword);
        Assertions.assertNull(response.getUsername(), "Failure response includes a username");
        Assertions.assertNull(response.getAuthToken(), "Failure response includes an authToken");
        Assertions.assertNotNull(response.getMessage(), "Failure response does not include a message");
    }

    @Test
    public void logoutSuccess() {
        facade.register(testUsername, testPassword, testEmail);
        String token = facade.login(testUsername, testPassword).getAuthToken();
        LogoutResponse response = facade.logout(token);
        Assertions.assertNull(response.getMessage(), "Success response contains a message");
    }

    @Test
    public void logoutFail() {
        facade.register(testUsername, testPassword, testEmail);
        String token = "fake_token";
        LogoutResponse response = facade.logout(token);
        Assertions.assertNotNull(response.getMessage(), "Failure response does not contain a message");
    }

    @Test
    public void createGameSuccess() {
        String authToken = facade.register(testUsername, testPassword, testEmail).getAuthToken();
        CreateGameResponse response = facade.createGame(authToken, testGameName_1);
        Assertions.assertEquals(1, response.getGameID(), "Success response does not contain a game id of 1. Game id should equal 1 because it is the first game in the database.");
        Assertions.assertNull(response.getMessage(), "Success response contains a message.");
    }

    @Test
    public void createGameFail() {
        CreateGameResponse response = facade.createGame(null, testGameName_1);
        Assertions.assertNull(response.getGameID(), "Failure response contains a game id");
        Assertions.assertNotNull(response.getMessage(), "Failure response does not contain a message");
    }

    @Test
    public void listGamesSuccess() {
        String authToken = facade.register(testUsername, testPassword, testEmail).getAuthToken();
        facade.createGame(authToken, testGameName_1);
        facade.createGame(authToken, testGameName_2);
        ListGamesResponse response = facade.listGames(authToken);
        Assertions.assertEquals(response.getGamesList().size(), 2, "Success response does not contain a list with two games");
        Assertions.assertNull(response.getMessage(), "Success response contains a message");
    }

    @Test
    public void listGamesFail() {
        String authToken = facade.register(testUsername, testPassword, testEmail).getAuthToken();
        facade.createGame(authToken, testGameName_1);
        facade.createGame(authToken, testGameName_2);
        facade.logout(authToken);
        ListGamesResponse response = facade.listGames(authToken);
        Assertions.assertNull(response.getGamesList(), "Failure response contains a list of games");
        Assertions.assertNotNull(response.getMessage(), "Failure response does not contain a message");
    }

    @Test
    public void joinGameSuccess() {
        String authToken = facade.register(testUsername, testPassword, testEmail).getAuthToken();
        int gameId = facade.createGame(authToken, testGameName_1).getGameID();
        JoinGameResponse response = facade.joinGame(authToken, ChessGame.TeamColor.WHITE, gameId);
        Set<Game> gameList = facade.listGames(authToken).getGamesList();
        Assertions.assertNull(response.getMessage(), "Success response contains a message");
        Assertions.assertEquals(testUsername, gameList.iterator().next().getWhiteUsername(), "Game retrieved from database does not have the correct white username");
    }

    @Test
    public void joinGameFail() {
        String authToken = facade.register(testUsername, testPassword, testEmail).getAuthToken();
        int gameId = facade.createGame(authToken, testGameName_1).getGameID();
        facade.logout(authToken);
        JoinGameResponse response = facade.joinGame(authToken, ChessGame.TeamColor.WHITE, gameId);
        Assertions.assertNotNull(response.getMessage(), "Failure response does not contain a message");
        authToken = facade.login(testUsername, testPassword).getAuthToken();
        Assertions.assertNull(facade.listGames(authToken).getGamesList().iterator().next().getWhiteUsername(), "Game retrieved from database includes a white username");
    }
}
