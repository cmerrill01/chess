import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import responses.ClearApplicationResponse;
import responses.LoginResponse;
import responses.LogoutResponse;
import responses.RegisterResponse;
import server.ServerFacade;

public class ClientTests {

    private static ServerFacade facade;
    private static final String serverUrl = "http://localhost:8080";
    private static final String testUsername = "john";
    private static final String testPassword = "sltw316";
    private static final String testEmail = "beloved@patmos.com";

    @BeforeAll
    public static void initialize() {
        facade = new ServerFacade(serverUrl);
    }

    @BeforeEach
    public void clearDatabase() {
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

}
