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

import java.util.UUID;

public class DAOTests {

    private static Database db;
    private static User testUser;
    private static AuthToken testToken;
    private static Game testGame;

    @BeforeAll
    public static void initializeVariables() {
        db = new Database();
        testUser = new User("peter", "th!sR0ck", "peter@galilee.net");
        testToken = new AuthToken("peter", UUID.randomUUID().toString());
        testGame = new Game("joppa");
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
            Assertions.assertEquals(testUser, userDAO.findUser(testUser.getUsername()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertUserFailDuplicateUser() {
        UserDAO userDAO = new UserDAO(db);
        try {
            userDAO.insertUser(testUser);
            Assertions.assertThrows(DataAccessException.class, () -> userDAO.insertUser(testUser));
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
            Assertions.assertNull(userDAO.findUser(testUser.getUsername()));
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
