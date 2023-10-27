package passoffTests.serverTests;

import daos.memoryDatabase;
import models.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import requests.LoginRequest;
import requests.RegisterRequest;
import responses.ClearApplicationResponse;
import responses.LoginResponse;
import responses.RegisterResponse;
import services.ClearApplicationService;
import services.LoginService;
import services.RegisterService;


public class ServiceTests {

    private static memoryDatabase db;
    private static AuthToken testToken;
    private static User testUser;
    private static Game testGame;

    @BeforeAll
    public static void initializeVariables() {
        db = new memoryDatabase();
        testToken = new AuthToken("test_username", "test_token");
        testUser = new User("test_username", "test_password", "test_email");
        testGame = new Game("test_game_name", 1);
    }

    @BeforeEach
    public void clearDatabase() {
        db.getAuthTokenTable().clear();
        db.getUserTable().clear();
        db.getGameTable().clear();
    }

    @Test
    public void clearSuccess() {
        db.getAuthTokenTable().put(testToken.getUsername(), testToken);
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
        Assertions.assertNotNull(db.getAuthTokenTable().get(testUser.getUsername()), "No AuthToken found for user");
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
        LoginResponse response = service.login(request);

        Assertions.assertNotNull(db.getAuthTokenTable().get(testUser.getUsername()), "AuthToken not found in database for logged-in user");
        Assertions.assertEquals(response.getUsername(), testUser.getUsername(), "Response does not include the user's username");
        Assertions.assertEquals(response.getAuthToken(), db.getAuthTokenTable().get(testUser.getUsername()).getAuthToken(), "Response does not include the correct token");
        Assertions.assertNull(response.getMessage(), "Response should not include a message");
    }

    @Test
    public void loginFailUsernameIncorrect() {
        db.getUserTable().put(testUser.getUsername(), testUser);

        LoginRequest request = new LoginRequest("incorrect_username", "test_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request);

        Assertions.assertNull(db.getAuthTokenTable().get(request.getUsername()), "Non-existent user should not have been given a token");
        Assertions.assertNull(response.getUsername(), "Response should not include a username");
        Assertions.assertNull(response.getAuthToken(), "Response should not include an AuthToken");
        Assertions.assertEquals(response.getMessage(), "Error: username does not exist", "Response message incorrect");
    }

    @Test
    public void loginFailPasswordIncorrect() {
        db.getUserTable().put(testUser.getUsername(), testUser);

        LoginRequest request = new LoginRequest("test_username", "incorrect_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request);

        Assertions.assertNull(db.getAuthTokenTable().get(request.getUsername()), "User should not have been given an AuthToken");
        Assertions.assertNull(response.getUsername(), "Response should not include a username");
        Assertions.assertNull(response.getAuthToken(), "Response should not include an AuthToken");
        Assertions.assertEquals(response.getMessage(), "Error: unauthorized", "Response message incorrect");
    }

    @Test
    public void loginFailAlreadyLoggedIn() {
        db.getUserTable().put(testUser.getUsername(), testUser);
        db.getAuthTokenTable().put(testToken.getUsername(), testToken);

        LoginRequest request = new LoginRequest("test_username", "test_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request);

        Assertions.assertEquals(db.getAuthTokenTable().get(testToken.getUsername()).getAuthToken(), testToken.getAuthToken(), "Token should not have been replaced");
        Assertions.assertEquals(db.getAuthTokenTable().size(), 1, "A new token should not have been created");
        Assertions.assertNull(response.getUsername(), "Response should not include a username");
        Assertions.assertNull(response.getAuthToken(), "Response should not include an AuthToken");
        Assertions.assertEquals(response.getMessage(), "Error: already logged in", "Response message incorrect");
    }

}
