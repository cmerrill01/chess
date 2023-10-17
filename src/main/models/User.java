package models;

public class User {

    /**
     * the user's username, used to log in and join games
     */
    private String username;
    /**
     * the user's password, used to log in. This is the only field that can be changed after the account is
     * created
     */
    private String password;
    /**
     * the user's email, which is not used for anything
     */
    private String email;

    /**
     * Create a new user account (not yet added to the database)
     * @param username the user's username, used to log in and join games
     * @param password the user's password, used to log in
     * @param email the user's email, which is not used for anything
     */
    public User(String username, String password, String email) {

    }

    public String getUsername() {
        return null;
    }

    public String getPassword() {
        return null;
    }

    public void setPassword(String password) {

    }

    public void setEmail(String email) {

    }
}
