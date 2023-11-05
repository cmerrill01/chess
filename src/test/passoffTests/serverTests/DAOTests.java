package passoffTests.serverTests;

import daos.AuthDAO;
import daos.GameDAO;
import daos.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.utils.Assert;

import java.util.UUID;

public class DAOTests {

    private static Database db;
    private static User testUser;
    private static User testUser2;
    private static AuthToken testToken;
    private static AuthToken testToken2;
    private static Game testGame;
    private static Game testGame2;

    @BeforeAll
    public static void initializeVariables() {
        db = new Database();
        testUser = new User("peter", "th!sR0ck", "peter@galilee.net");
        testUser2 = new User("paul", "2G3nt!l3s", "paul@tarsus.com");
        testToken = new AuthToken(testUser.getUsername(), UUID.randomUUID().toString());
        testToken2 = new AuthToken(testUser2.getUsername(), UUID.randomUUID().toString());
        testGame = new Game("joppa");
        testGame2 = new Game("damascus");
    }

    @BeforeEach
    public void clearDatabase() {
        UserDAO userDAO = new UserDAO(db);
        AuthDAO authDAO = new AuthDAO(db);
        GameDAO gameDAO = new GameDAO(db);
        try {
            userDAO.clearUsers();
            authDAO.clearAuthTokens();
            gameDAO.clearGames();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertUserSuccess() {
        UserDAO userDAO = new UserDAO(db);
        try {
            userDAO.insertUser(testUser);
            Assertions.assertEquals(testUser, userDAO.findUser(testUser.getUsername()), "Failed to find user after insertion into the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertUserFailDuplicateUser() {
        UserDAO userDAO = new UserDAO(db);
        try {
            userDAO.insertUser(testUser);
            Assertions.assertThrows(DataAccessException.class, () -> userDAO.insertUser(testUser), "Insertion of a duplicate user should not be allowed");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void clearUsersSuccess() {
        UserDAO userDAO = new UserDAO(db);
        try {
            userDAO.insertUser(testUser);
            userDAO.clearUsers();
            Assertions.assertNull(userDAO.findUser(testUser.getUsername()), "No users should be retrieved from the database after it has been cleared");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findUserSuccess() {
        UserDAO userDAO = new UserDAO(db);
        try {
            userDAO.insertUser(testUser);
            userDAO.insertUser(testUser2);
            User userFromDB = userDAO.findUser(testUser.getUsername());
            Assertions.assertEquals(testUser, userFromDB, "Failed to retrieve the correct user information from the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findUserFail() {
        UserDAO userDAO = new UserDAO(db);
        try {
            userDAO.insertUser(testUser);
            Assertions.assertNull(userDAO.findUser(testUser2.getUsername()), "The database should not return a user with a username not in the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertAuthTokenSuccess() {
        AuthDAO authDAO = new AuthDAO(db);
        try {
            authDAO.insertAuthToken(testToken);
            Assertions.assertEquals(testToken, authDAO.findAuthToken(testToken.authToken()), "Unable to find expected AuthToken");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertAuthTokenFail() {
        AuthDAO authDAO = new AuthDAO(db);
        try {
            authDAO.insertAuthToken(testToken);
            Assertions.assertThrows(DataAccessException.class, () -> authDAO.insertAuthToken(testToken));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAuthTokenSuccess() {
        AuthDAO authDAO = new AuthDAO(db);
        try {
            authDAO.insertAuthToken(testToken);
            authDAO.insertAuthToken(testToken2);
            AuthToken tokenFromDB = authDAO.findAuthToken(testToken.authToken());
            Assertions.assertEquals(testToken, tokenFromDB);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAuthTokenFail() {
        AuthDAO authDAO = new AuthDAO(db);
        try {
            authDAO.insertAuthToken(testToken);
            Assertions.assertNull(authDAO.findAuthToken(testToken2.authToken()), "The database should not return a token not in the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeAuthTokenSuccess() {
        AuthDAO authDAO = new AuthDAO(db);
        try {
            authDAO.insertAuthToken(testToken);
            authDAO.insertAuthToken(testToken2);
            authDAO.removeAuthToken(testToken.authToken());
            Assertions.assertNull(authDAO.findAuthToken(testToken.authToken()), "A token should not be found in the database after it is removed");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeAuthTokenFail() {
        AuthDAO authDAO = new AuthDAO(db);
        try {
            authDAO.insertAuthToken(testToken2);
            Assertions.assertEquals(testToken2, authDAO.findAuthToken(testToken2.authToken()), "No token should have been removed from the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearAuthTokensSuccess() {
        AuthDAO authDAO = new AuthDAO(db);
        try {
            authDAO.insertAuthToken(testToken);
            authDAO.insertAuthToken(testToken2);
            authDAO.clearAuthTokens();
            Assertions.assertNull(authDAO.findAuthToken(testToken.authToken()), "No tokens should have been found in the database");
            Assertions.assertNull(authDAO.findAuthToken(testToken2.authToken()), "No tokens should have been found in the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertGameSuccess() {
        GameDAO gameDAO = new GameDAO(db);
        try {
            int testGameId = gameDAO.insertGame(testGame);
            int testGame2Id = gameDAO.insertGame(testGame2);
            Assertions.assertEquals(testGame, gameDAO.findGame(testGameId), "Failed to find the game at the expected ID");
            Assertions.assertEquals(testGame2, gameDAO.findGame(testGame2Id), "Failed to find the game at the expected ID");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void insertGameFail() {
        GameDAO gameDAO = new GameDAO(db);
        Game nullGameName = new Game(null);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.insertGame(nullGameName), "A game with a null game name should not be inserted into the database");
    }

    @Test
    void findGameSuccess() {
        GameDAO gameDAO = new GameDAO(db);
        try {
            int testGameId = gameDAO.insertGame(testGame);
            testGame.setGameID(testGameId);
            int testGame2Id = gameDAO.insertGame(testGame2);
            testGame2.setGameID(testGame2Id);
            Assertions.assertEquals(testGame, gameDAO.findGame(testGameId), "Failed to find the game at the expected ID");
            Assertions.assertEquals(testGame2, gameDAO.findGame(testGame2Id), "Failed to find the game at the expected ID");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findGameFail() {
        GameDAO gameDAO = new GameDAO(db);
        try {
            int testGameId = gameDAO.insertGame(testGame);
            Assertions.assertNull(gameDAO.findGame(testGameId + 1), "There should not be a game at the index above the most recent game inserted");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void clearGamesSuccess() {
        GameDAO gameDAO = new GameDAO(db);
        try {
            int testGameId = gameDAO.insertGame(testGame);
            int testGame2Id = gameDAO.insertGame(testGame2);
            gameDAO.clearGames();
            Assertions.assertNull(gameDAO.findGame(testGameId), "Cleared database should not include any games");
            Assertions.assertNull(gameDAO.findGame(testGame2Id), "Cleared database should not include any games");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
