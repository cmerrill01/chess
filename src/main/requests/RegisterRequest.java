package requests;

public class RegisterRequest {

    /**
     * the new user's username, which will be used to create a new User in the database
     */
    private String username;
    /**
     * the new user's password, which will be used to create a new User in the database
     */
    private String password;
    /**
     * the new user's email address, which will be used to create a new User in the database
     */
    private String email;

    /**
     * Create a new request to add a user to the database.
     * Provide the user's username, password, and email address
     */
    public RegisterRequest(String username, String password, String email) {

    }


    public String getUsername() {
        return null;
    }


    public String getPassword() {
        return null;
    }


    public String getEmail() {
        return null;
    }

}
