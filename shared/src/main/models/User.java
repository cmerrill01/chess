package models;

import java.util.Objects;

public class User {

    /**
     * the user's username, used to log in and join games
     */
    private final String username;
    /**
     * the user's password, used to log in. This is the only field that can be changed after the account is
     * created
     */
    private final String password;
    /**
     * the user's email, which is not used for anything
     */
    private final String email;

    /**
     * Create a new user account (not yet added to the database)
     * @param username the user's username, used to log in and join games
     * @param password the user's password, used to log in
     * @param email the user's email, which is not used for anything
     */
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, email);
    }
}
