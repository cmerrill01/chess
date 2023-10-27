package passoffTests.serverTests;

import daos.memoryDatabase;
import models.*;
import org.junit.jupiter.api.*;
import services.ClearApplicationService;

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

    @Test
    public void clearSuccess() {
        db.getAuthTokenTable().put(testToken.getUsername(), testToken);
        db.getUserTable().put(testUser.getUsername(), testUser);
        db.getGameTable().put(testGame.getGameID(), testGame);

        new ClearApplicationService().clearApplication(db);
        Assertions.assertTrue(db.getAuthTokenTable().isEmpty(), "AuthToken table not cleared");
        Assertions.assertTrue(db.getUserTable().isEmpty(), "User table not cleared");
        Assertions.assertTrue(db.getGameTable().isEmpty(), "Game table not cleared");
    }

}
