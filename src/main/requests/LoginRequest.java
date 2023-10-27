package requests;

public class LoginRequest {

    /**
     * the username provided to attempt to log in
     */
    private String username;

    /**
     * the password provided to attempt to log in
     */
    private String password;

    /**
     * Create a new request to log in to an existing user account
     * @param username the username provided to attempt to log in
     * @param password the password provided to attempt to log in
     */
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
