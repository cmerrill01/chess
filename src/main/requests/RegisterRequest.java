package requests;

public class RegisterRequest {

    /**
     * the new user's username, which will be used to create a new User in the database
     */
    private final String username;
    /**
     * the new user's password, which will be used to create a new User in the database
     */
    private final String password;
    /**
     * the new user's email address, which will be used to create a new User in the database
     */
    private final String email;

    /**
     * Create a new request to add a user to the database.
     * Provide the user's username, password, and email address
     */
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }


    public String getEmail() {
        return email;
    }

}
