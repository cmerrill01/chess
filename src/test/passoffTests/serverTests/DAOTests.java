package passoffTests.serverTests;

import chess.ChessGame;
import chess.InvalidMoveException;
import daos.AuthDAO;
import daos.GameDAO;
import daos.UserDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import chess.ChessBoardImpl;
import chess.ChessGameImpl;
import chess.ChessMoveImpl;
import chess.ChessPositionImpl;
import models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;
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
    public void clearData() {
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

        ChessGame defaultGame = new ChessGameImpl();
        if (!testGame.getGame().equals(defaultGame)) testGame.setGame(new ChessGameImpl());
        if (!testGame2.getGame().equals(defaultGame)) testGame2.setGame(new ChessGameImpl());

        if (testGame.getWhiteUsername() != null) testGame.setWhiteUsername(null);
        if (testGame.getBlackUsername() != null) testGame.setBlackUsername(null);
        if (testGame2.getWhiteUsername() != null) testGame2.setWhiteUsername(null);
        if (testGame2.getBlackUsername() != null) testGame2.setBlackUsername(null);
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

    @Test
    void findAllGamesSuccess() {
        GameDAO gameDAO = new GameDAO(db);
        try {
            int testGameID = gameDAO.insertGame(testGame);
            testGame.setGameID(testGameID);
            int testGame2ID = gameDAO.insertGame(testGame2);
            testGame2.setGameID(testGame2ID);
            Set<Game> games = new TreeSet<>();
            games.add(testGame);
            games.add(testGame2);
            Assertions.assertEquals(games, gameDAO.findAllGames(), "Failed to retrieve a list of all games from the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void findAllGamesFail() {
        GameDAO gameDAO = new GameDAO(db);
        Set<Game> games = new TreeSet<>();
        try {
            Assertions.assertEquals(games, gameDAO.findAllGames(), "Should return an empty set of games from the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeGameSuccess() {
        GameDAO gameDAO = new GameDAO(db);
        try {
            int testGameID = gameDAO.insertGame(testGame);
            testGame.setGameID(testGameID);
            int testGame2ID = gameDAO.insertGame(testGame2);
            testGame2.setGameID(testGame2ID);
            Set<Game> games = new TreeSet<>();
            games.add(testGame2);

            gameDAO.removeGame(testGameID);
            Assertions.assertEquals(games, gameDAO.findAllGames(), "First test game should have been removed");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void removeGameFail() {
        GameDAO gameDAO = new GameDAO(db);
        try {
            int testGameID = gameDAO.insertGame(testGame);
            testGame.setGameID(testGameID);
            int testGame2ID = gameDAO.insertGame(testGame2);
            testGame2.setGameID(testGame2ID);
            Set<Game> games = new TreeSet<>();
            games.add(testGame);
            games.add(testGame2);

            gameDAO.removeGame(testGame2ID + 1);
            Assertions.assertEquals(games, gameDAO.findAllGames(), "No game should have been removed when an invalid ID was provided");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void claimSpotInGameSuccess() {
        GameDAO gameDAO = new GameDAO(db);
        UserDAO userDAO = new UserDAO(db);
        AuthDAO authDAO = new AuthDAO(db);
        try {
            userDAO.insertUser(testUser);
            authDAO.insertAuthToken(testToken);
            int testGameID = gameDAO.insertGame(testGame);
            testGame.setGameID(testGameID);
            testGame.setBlackUsername(testUser.getUsername());

            gameDAO.claimSpotInGame(testUser.getUsername(), testGameID, ChessGame.TeamColor.BLACK);
            Assertions.assertEquals(testGame, gameDAO.findGame(testGameID), "Test user's username should have been added as the white username for the game in the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void claimSpotInGameFail() {
        GameDAO gameDAO = new GameDAO(db);
        UserDAO userDAO = new UserDAO(db);
        AuthDAO authDAO = new AuthDAO(db);
        try {
            userDAO.insertUser(testUser);
            authDAO.insertAuthToken(testToken);
            userDAO.insertUser(testUser2);
            authDAO.insertAuthToken(testToken2);
            int testGameID = gameDAO.insertGame(testGame);
            testGame.setGameID(testGameID);
            testGame.setWhiteUsername(testUser.getUsername());

            gameDAO.claimSpotInGame(testUser.getUsername(), testGameID, ChessGame.TeamColor.WHITE);
            Assertions.assertThrows(DataAccessException.class, () ->
                    gameDAO.claimSpotInGame(testUser2.getUsername(), testGameID, ChessGame.TeamColor.WHITE),
                    "Test user 2 should not be able to claim to be white in the test game if test user has already claimed it");
            Assertions.assertEquals(testGame, gameDAO.findGame(testGameID), "Test user's username should have been added as the white username for the game in the database");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateGameSuccess() {
        GameDAO gameDAO = new GameDAO(db);
        try {
            int testGameID = gameDAO.insertGame(testGame);
            testGame.setGameID(testGameID);

            ChessGame updatedGame = testGame.getGame();
            updatedGame.setBoard(new ChessBoardImpl());
            updatedGame.getBoard().resetBoard();
            updatedGame.makeMove(new ChessMoveImpl(new ChessPositionImpl(2, 4), new ChessPositionImpl(4, 4)));

            gameDAO.updateGame(testGameID, updatedGame);
            Assertions.assertEquals(testGame, gameDAO.findGame(testGameID), "Updated chess game does not match the chess game in the database");
        } catch (DataAccessException | InvalidMoveException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void updateGameFail() {
        GameDAO gameDAO = new GameDAO(db);
        try {
            int testGameID = gameDAO.insertGame(testGame);
            testGame.setGameID(testGameID);

            Assertions.assertThrows(DataAccessException.class, () ->
                    gameDAO.updateGame(testGameID, null));
            Assertions.assertEquals(testGame, gameDAO.findGame(testGameID), "Chess game should not have been updated in database when provided with a null game");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
