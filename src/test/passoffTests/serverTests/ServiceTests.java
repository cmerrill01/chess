package passoffTests.serverTests;

import chess.ChessGame;
import daos.AuthDAO;
import daos.GameDAO;
import daos.UserDAO;
import dataAccess.DataAccessException;
import models.*;
import org.junit.jupiter.api.*;
import requests.LoginRequest;
import requests.*;
import responses.*;
import services.*;
import java.util.Set;
import java.util.TreeSet;
import dataAccess.Database;


public class ServiceTests {

    private static Database db;
    private static UserDAO testUserDAO;
    private static AuthDAO testAuthDAO;
    private static GameDAO testGameDAO;
    private static AuthToken testToken;
    private static User testUser;
    private static Game testGame;
    private static Game otherTestGame;

    @BeforeAll
    public static void initializeVariables() {
        db = new Database();
        testUserDAO = new UserDAO(db);
        testAuthDAO = new AuthDAO(db);
        testGameDAO = new GameDAO(db);
        testToken = new AuthToken("test_username", "test_token");
        testUser = new User("test_username", "test_password", "test_email");
        testGame = new Game("test_game_name");
        testGame.setGameID(1);
        otherTestGame = new Game("other_game_name");
        otherTestGame.setGameID(2);
    }

    @BeforeEach
    public void clearDatabase() {
        try {
            testAuthDAO.clearAuthTokens();
            testUserDAO.clearUsers();
            testGameDAO.clearGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        testGame.setWhiteUsername(null);
        testGame.setBlackUsername(null);
        otherTestGame.setWhiteUsername(null);
        otherTestGame.setBlackUsername(null);
    }

    @Test
    public void clearSuccess() {
        try {
            testAuthDAO.insertAuthToken(testToken);
            testUserDAO.insertUser(testUser);
            int testGameId = testGameDAO.insertGame(testGame);
            testGame.setGameID(testGameId);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        ClearApplicationResponse successResponse = new ClearApplicationResponse();

        ClearApplicationResponse response = new ClearApplicationService().clearApplication(db);
        try {
            Assertions.assertNull(testAuthDAO.findAuthToken(testToken.authToken()), "AuthToken table not cleared");
            Assertions.assertNull(testUserDAO.findUser(testUser.getUsername()), "User table not cleared");
            Assertions.assertNull(testGameDAO.findGame(testGame.getGameID()), "Game table not cleared");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(successResponse, response);
    }

    @Test
    public void registerSuccess() {
        RegisterRequest request = new RegisterRequest("test_username", "test_password", "test_email");
        RegisterService service = new RegisterService();
        RegisterResponse response = service.register(request, db);

        try {
            Assertions.assertEquals(testUser, testUserDAO.findUser(testUser.getUsername()), "User not found in database");
            Assertions.assertNotNull(testAuthDAO.findAuthToken(response.getAuthToken()), "No AuthToken found for user");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(testUser.getUsername(), response.getUsername(), "Response does not include the correct username");
        Assertions.assertNotNull(response.getAuthToken(), "Response does not include the user's AuthToken");
    }

    @Test
    public void registerFailAlreadyTaken() {
        try {
            testUserDAO.insertUser(testUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        RegisterRequest request = new RegisterRequest("test_username", "test_password", "test_email");
        RegisterService service = new RegisterService();
        RegisterResponse response = service.register(request, db);

        try {
            Assertions.assertEquals(testUser, testUserDAO.findUser(testUser.getUsername()), "The user should be found in the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNull(response.getUsername(), "The response should not contain a username");
        Assertions.assertNull(response.getAuthToken(), "The response should not contain an AuthToken");
        Assertions.assertEquals(response.getMessage(), "Error: already taken", "The response message is incorrect");
    }

    @Test
    public void loginSuccess() {
        try {
            testUserDAO.insertUser(testUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        LoginRequest request = new LoginRequest("test_username", "test_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request, db);

        try {
            Assertions.assertNotNull(testAuthDAO.findAuthToken(response.getAuthToken()), "Response does not include the correct token");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(testUser.getUsername(), response.getUsername(), "Response does not include the user's username");
        Assertions.assertNull(response.getMessage(), "Response should not include a message");
    }

    @Test
    public void loginFailUsernameIncorrect() {
        try {
            testUserDAO.insertUser(testUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        LoginRequest request = new LoginRequest("incorrect_username", "test_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request, db);


        Assertions.assertNull(response.getUsername(), "Response should not include a username");
        Assertions.assertNull(response.getAuthToken(), "Response should not include an AuthToken");
        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void loginFailPasswordIncorrect() {
        try {
            testUserDAO.insertUser(testUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        LoginRequest request = new LoginRequest("test_username", "incorrect_password");
        LoginService service = new LoginService();
        LoginResponse response = service.login(request, db);

        Assertions.assertNull(response.getUsername(), "Response should not include a username");
        Assertions.assertNull(response.getAuthToken(), "Response should not include an AuthToken");
        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void logoutSuccess() {
        try {
            testUserDAO.insertUser(testUser);
            testAuthDAO.insertAuthToken(testToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        LogoutRequest request = new LogoutRequest("test_token");
        LogoutService service = new LogoutService();
        LogoutResponse response = service.logout(request, db);

        try {
            Assertions.assertNull(testAuthDAO.findAuthToken(testToken.authToken()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNull(response.getMessage(), "The response should not include a message");
    }

    @Test
    public void logoutFail() {
        try {
            testUserDAO.insertUser(testUser);
            testAuthDAO.insertAuthToken(testToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        LogoutRequest request = new LogoutRequest("incorrect_token");
        LogoutService service = new LogoutService();
        LogoutResponse response = service.logout(request, db);

        try {
            Assertions.assertNotNull(testAuthDAO.findAuthToken(testToken.authToken()), "Token should not have been removed");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void listGamesSuccess() {
        try {
            testUserDAO.insertUser(testUser);
            testAuthDAO.insertAuthToken(testToken);
            testGameDAO.insertGame(testGame);
            testGameDAO.insertGame(otherTestGame);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Set<Game> gameList = new TreeSet<>();
        gameList.add(testGame);
        gameList.add(otherTestGame);

        ListGamesRequest request = new ListGamesRequest(testToken.authToken());
        ListGamesService service = new ListGamesService();
        ListGamesResponse response = service.listGames(request, db);

        Assertions.assertEquals(gameList, response.getGamesList(), "Response does not return the correct list of games");
        Assertions.assertNull(response.getMessage(), "Response should not include a message");
    }

    @Test
    public void listGamesFail() {
        try {
            testUserDAO.insertUser(testUser);
            testAuthDAO.insertAuthToken(testToken);
            testGameDAO.insertGame(testGame);
            testGameDAO.insertGame(otherTestGame);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        ListGamesRequest request = new ListGamesRequest("incorrect_token");
        ListGamesService service = new ListGamesService();
        ListGamesResponse response = service.listGames(request, db);

        Assertions.assertNull(response.getGamesList(), "Response should not include a list of games");
        Assertions.assertEquals("Error: unauthorized", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void createGameSuccess() {
        try {
            testUserDAO.insertUser(testUser);
            testAuthDAO.insertAuthToken(testToken);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        CreateGameRequest request = new CreateGameRequest("test_game");
        CreateGameService service = new CreateGameService();
        CreateGameResponse response = service.createGame(request, db);

        try {
            Assertions.assertNotNull(testGameDAO.findGame(response.getGameID()), "No game was added to the database with an id of 1");
            Assertions.assertEquals("test_game", testGameDAO.findGame(response.getGameID()).getGameName(), "No game was added to the database with the requested name");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createGameFail() {
        try {
            testUserDAO.insertUser(testUser);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        CreateGameRequest request = new CreateGameRequest(null);
        CreateGameService service = new CreateGameService();
        CreateGameResponse response = service.createGame(request, db);

        try {
            Assertions.assertTrue(testGameDAO.findAllGames().isEmpty(), "No game should have been added to the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("Error: bad request", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void joinGameSuccessPlayer() {
        try {
            testUserDAO.insertUser(testUser);
            testAuthDAO.insertAuthToken(testToken);
            testGameDAO.insertGame(testGame);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        JoinGameRequest request = new JoinGameRequest(testGame.getGameID(), ChessGame.TeamColor.WHITE);
        request.setUsername(testUser.getUsername());
        JoinGameService service = new JoinGameService();
        JoinGameResponse response = service.joinGame(request, db);

        try {
            Assertions.assertEquals(testUser.getUsername(), testGameDAO.findGame(request.getGameID()).getWhiteUsername(), "White username does not equal the requested username");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertNull(response.getMessage(), "Response should not include a message");
    }

    @Test
    public void joinGameSuccessViewer() {
        try {
            testUserDAO.insertUser(testUser);
            testAuthDAO.insertAuthToken(testToken);
            testGameDAO.insertGame(testGame);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        JoinGameRequest request = new JoinGameRequest(testGame.getGameID());
        JoinGameService service = new JoinGameService();
        request.setUsername(testUser.getUsername());
        JoinGameResponse response = service.joinGame(request, db);

        Assertions.assertNull(testGame.getWhiteUsername(), "White username should be null");
        Assertions.assertNull(testGame.getBlackUsername(), "Black username should be null");
        Assertions.assertNull(response.getMessage(), "Response should not include a message");
    }

    @Test
    public void joinGameFailAlreadyTaken() {
        int testGameId;
        try {
            testUserDAO.insertUser(testUser);
            testAuthDAO.insertAuthToken(testToken);
            testGameId = testGameDAO.insertGame(testGame);
            testGameDAO.claimSpotInGame("other_username", testGameId, ChessGame.TeamColor.BLACK);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        JoinGameRequest request = new JoinGameRequest(testGame.getGameID(), ChessGame.TeamColor.BLACK);
        JoinGameService service = new JoinGameService();
        request.setUsername(testUser.getUsername());
        JoinGameResponse response = service.joinGame(request, db);

        try {
            Assertions.assertEquals("other_username", testGameDAO.findGame(testGameId).getBlackUsername(), "Black username should not have been replaced");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals("Error: already taken", response.getMessage(), "Response message incorrect");
    }

    @Test
    public void joinGameFailBadRequest() {
        try {
            testUserDAO.insertUser(testUser);
            testAuthDAO.insertAuthToken(testToken);
            testGameDAO.insertGame(testGame);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        JoinGameRequest request = new JoinGameRequest(3, ChessGame.TeamColor.WHITE);
        JoinGameService service = new JoinGameService();
        request.setUsername(testUser.getUsername());
        JoinGameResponse response = service.joinGame(request, db);

        Assertions.assertNull(testGame.getWhiteUsername(), "White username should be null");
        Assertions.assertNull(testGame.getBlackUsername(), "Black username should be null");
        Assertions.assertEquals("Error: bad request", response.getMessage(), "Response message incorrect");
    }

}



