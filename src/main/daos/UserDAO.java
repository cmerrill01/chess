package daos;

import dataAccess.DataAccessException;
import models.User;
import java.util.Set;

public class UserDAO {

    /**
     * the set of all users in the database
     */
    private Set<User> users;

    /**
     * Create a new DAO for the users in the database
     */
    public UserDAO() {

    }

    /**
     * insert a new user into the database
     * @param userToInsert the user to be inserted into the database
     * @throws DataAccessException if the user is not successfully inserted into the database
     */
    public void insertUser(User userToInsert) throws DataAccessException {

    }

    /**
     * find a user in the database based on their username
     * @param username the username of the user we are looking for
     * @return the user, if found; otherwise, null
     * @throws DataAccessException if there was a problem accessing the data
     */
    public User findUser(String username) throws DataAccessException {
        return null;
    }

    /**
     * change a user's password in the database
     * @param username the username of the user whose password is to be changed
     * @throws DataAccessException if the password is not successfully changed
     */
    public void changePassword(String username) throws DataAccessException {

    }

    /**
     * remove a user from the database
     * @param username the username of the user to be removed
     * @throws DataAccessException if the user is not successfully removed from the database
     */
    public void removeUser(String username) throws DataAccessException {

    }

}
