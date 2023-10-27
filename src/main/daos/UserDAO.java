package daos;

import dataAccess.DataAccessException;
import models.User;

import javax.xml.crypto.Data;
import java.util.Map;
import java.util.TreeMap;

public class UserDAO {

    /**
     * the set of all users in the database
     */
    private Map<String, User> users;

    /**
     * Create a new DAO for the users in the database
     */
    public UserDAO() {

    }

    /**
     * Create a new DAO for users in a memory-based database
     * @param memoryUserTable a pointer to the table of users in the memory-based database
     */
    public UserDAO(Map<String, User> memoryUserTable) {
        users = memoryUserTable;
    }

    /**
     * insert a new user into the database
     * @param userToInsert the user to be inserted into the database
     * @throws DataAccessException if the user is not successfully inserted into the database
     */
    public void insertUser(User userToInsert) throws DataAccessException {
        if (users.containsKey(userToInsert.getUsername())) throw new DataAccessException("Error: already taken");
        users.put(userToInsert.getUsername(), userToInsert);
    }

    /**
     * find a user in the database based on their username
     * @param username the username of the user we are looking for
     * @return the user, if found; otherwise, null
     * @throws DataAccessException if there was a problem accessing the data
     */
    public User findUser(String username) throws DataAccessException {
        return users.get(username);
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

    /**
     * clear all users from the database
     * @throws DataAccessException if the users are not successfully removed from the database
     */
    public void clearUsers() throws DataAccessException {
        users.clear();
    }

}
