package passoffTests.serverTests;

import daos.memoryDatabase;
import models.*;
import org.junit.jupiter.api.*;
import requests.LoginRequest;
import requests.*;
import responses.*;
import services.*;
import java.util.Set;
import java.util.TreeSet;


public class ServiceTests {

    private static memoryDatabase db;
    private static AuthToken testToken;
    private static User testUser;
    private static Game testGame;
    private static Game otherTestGame;

    @BeforeAll
    public static void initializeVariables() {
        db = new memoryDatabase();
        testToken = new AuthToken("test_username", "test_token");
        testUser = new User("test_username", "test_password", "test_email");
        testGame = new Game("test_game_name");
        otherTestGame = new Game("other_game_name");
    }

    @BeforeEach
    public void clearDatabase() {
        db.getAuthTokenTable().clear();
        db.getUserTable().clear();
        db.getGameTable().clear();
    }

    @Test
    public void clearSuccess() {
        db.getAuthTokenTable().put(testToken.getAuthToken(), testToken);
        db.getUserTable().put(testUser.getUsername(), testUser);
        db.getGameTable().put(testGame.getGameID(), testGame);
        ClearApplicationResponse successResponse = new ClearApplicationResponse();

        ClearApplicationResponse response = new ClearApplicationService().clearApplication(db);
        Assertions.assertTrue(db.getAuthTokenTable().isEmpty(), "AuthToken table not cleared");
        Assertions.assertTrue(db.getUserTable().isEmpty(), "User table not cleared");
        Assertions.assertTrue(db.getGameTable().isEmpty(), "Game table not cleared");
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void registerSuccess() {
        RegisterRequest request = new RegisterRequest("test_username", "test_password", "test_email");
        RegisterService service = new RegisterService();
        RegisterResponse response = service.register(request, db);

        Assertions.assertEquals(testUser, db.getUserTable().get(testUser.getUsername()), "User not found in database");
        Assertions.assertNotNull(db.getAuthTokenTable().get(response.getAuthToken()), "No AuthToken found for user");
        Assertions.assertEquals(testUser.getUsername(), response.getUsername(), "Response does not include the correct username");
        Assertions.assertNotNull(response.getAuthToken(), "Response does not include the user's AuthToken");
    }

    @Test
    public void registerFailAlreadyTaken() {
        db.getUserTable().put(testUser.getUsername(), testUser);

        RegisterRequest request = new RegisterRequest("test_username", "test_password", "test_email");
        RegisterService service = new RegisterService();
        RegisterResponse response = service.register(request, db);

        Assertions.assertEquals(db.getUserTable().size(), 1, "Only one user should be found in the database");
        Assertions.assertTrue(db.getAuthTokenTable().isEmpty(), "No tokens should be found in the database");
        Assertions.assertNull(response.getUsername(), "The response should not contain a username");
        Assertions.assertNull(response.getAuthToken(), "The response should not contain an AuthToken");
        Assertions.assertEquals(response.getMessage(), "Error: already taken", "The response message is incorrect");
    }

    @Test
    public void loginSuccess() {
        db.getUserTable().put(testUser.getUsername(), testUser);

        LoginRequest request = new LoginRequest("test_username", "test_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request, db);

        Assertions.assertNotNull(db.getAuthTokenTable().get(response.getAuthToken()), "AuthToken not found in database for logged-in user");
        Assertions.assertEquals(testUser.getUsername(), response.getUsername(), "Response does not include the user's username");
        Assertions.assertEquals(db.getAuthTokenTable().get(response.getAuthToken()).getAuthToken(), response.getAuthToken(), "Response does not include the correct token");
        Assertions.assertNull(response.getMessage(), "Response should not include a message");
    }

    @Test
    public void loginFailUsernameIncorrect() {
        db.getUserTable().put(testUser.getUsername(), testUser);

        LoginRequest request = new LoginRequest("incorrect_username", "test_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request, db);

        Assertions.assertNull(db.getAuthTokenTable().get(request.getUsername()), "Non-existent user should not have been given a token");
        Assertions.assertNull(response.getUsername(), "Response should not include a username");
        Assertions.assertNull(response.getAuthToken(), "Response should not include an AuthToken");
        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void loginFailPasswordIncorrect() {
        db.getUserTable().put(testUser.getUsername(), testUser);

        LoginRequest request = new LoginRequest("test_username", "incorrect_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request, db);

        Assertions.assertNull(db.getAuthTokenTable().get(request.getUsername()), "User should not have been given an AuthToken");
        Assertions.assertNull(response.getUsername(), "Response should not include a username");
        Assertions.assertNull(response.getAuthToken(), "Response should not include an AuthToken");
        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Response message incorrect");
    }

    // If this is a problem, reactivate this test
    public void loginFailAlreadyLoggedIn() {
        db.getUserTable().put(testUser.getUsername(), testUser);
        db.getAuthTokenTable().put(testToken.getAuthToken(), testToken);

        LoginRequest request = new LoginRequest("test_username", "test_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request, db);

        Assertions.assertEquals(testToken, db.getAuthTokenTable().get(testToken.getAuthToken()), "Token should not have been replaced");
        Assertions.assertEquals(1, db.getAuthTokenTable().size(), "A new token should not have been created");
        Assertions.assertNull(response.getUsername(), "Response should not include a username");
        Assertions.assertNull(response.getAuthToken(), "Response should not include an AuthToken");
        Assertions.assertEquals("Error: already logged in", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void logoutSuccess() {
        db.getUserTable().put(testUser.getUsername(), testUser);
        db.getAuthTokenTable().put(testToken.getAuthToken(), testToken);

        LogoutRequest request = new LogoutRequest("test_token");
        LogoutService service = new LogoutService();
        LogoutResponse response = service.logout(request, db);

        Assertions.assertNull(db.getAuthTokenTable().get(testToken.getAuthToken()), "The token should have been removed from the database");
        Assertions.assertNull(response.getMessage(), "The response should not include a message");
    }

    @Test
    public void logoutFail() {
        db.getUserTable().put(testUser.getUsername(), testUser);
        db.getAuthTokenTable().put(testToken.getAuthToken(), testToken);

        LogoutRequest request = new LogoutRequest("incorrect_token");
        LogoutService service = new LogoutService();
        LogoutResponse response = service.logout(request, db);

        Assertions.assertNotNull(db.getAuthTokenTable().get(testToken.getAuthToken()), "Token should not have been removed");
        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void listGamesSuccess() {
        db.getUserTable().put(testUser.getUsername(), testUser);
        db.getAuthTokenTable().put(testToken.getAuthToken(), testToken);
        db.getGameTable().put(testGame.getGameID(), testGame);
        db.getGameTable().put(otherTestGame.getGameID(), otherTestGame);
        Set<Game> gameList = new TreeSet<>();
        gameList.add(testGame);
        gameList.add(otherTestGame);

        ListGamesRequest request = new ListGamesRequest(testToken.getAuthToken());
        ListGamesService service = new ListGamesService();
        ListGamesResponse response = service.listGames(request, db);

        Assertions.assertEquals(gameList, response.getGamesList(), "Response does not return the correct list of games");
        Assertions.assertNull(response.getMessage(), "Response should not include a message");
    }

    @Test
    public void listGamesFail() {
        db.getUserTable().put(testUser.getUsername(), testUser);
        db.getAuthTokenTable().put(testToken.getAuthToken(), testToken);
        db.getGameTable().put(testGame.getGameID(), testGame);
        db.getGameTable().put(otherTestGame.getGameID(), otherTestGame);
        Set<Game> gameList = new TreeSet<>();
        gameList.add(testGame);
        gameList.add(otherTestGame);

        ListGamesRequest request = new ListGamesRequest("incorrect_token");
        ListGamesService service = new ListGamesService();
        ListGamesResponse response = service.listGames(request, db);

        Assertions.assertNull(response.getGamesList(), "Response should not include a list of games");
        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void createGameSuccess() {
        db.getUserTable().put(testUser.getUsername(), testUser);
        db.getAuthTokenTable().put(testToken.getAuthToken(), testToken);

        CreateGameRequest request = new CreateGameRequest("test_game");
        CreateGameService service = new CreateGameService();
        CreateGameResponse response = service.createGame(request, db);

        Assertions.assertNotNull(db.getGameTable().get(response.getGameID()), "No game was added to the database with an id of 1");
        Assertions.assertEquals("test_game", db.getGameTable().get(response.getGameID()).getGameName(), "No game was added to the database with the requested name");
    }

    @Test
    public void createGameFail() {
        db.getUserTable().put(testUser.getUsername(), testUser);

        CreateGameRequest request = new CreateGameRequest(null);
        CreateGameService service = new CreateGameService();
        CreateGameResponse response = service.createGame(request, db);

        Assertions.assertTrue(db.getGameTable().isEmpty(), "No game should have been added to the database");
        Assertions.assertEquals("Error: bad request", response.getMessage(), "Response message incorrect");
    }

}
