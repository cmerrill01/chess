package daos;

import dataAccess.DataAccessException;
import dataAccess.Database;
import models.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class UserDAO {

    /*  public boolean example(String selectStatement, Database db) throws DataAccessException{
     *    var conn = db.getConnection();
     *    try (var preparedStatement = conn.prepareStatement(selectStatement)) {
     *        return preparedStatement.execute();
     *    } catch (SQLException ex) {
     *        throw new DataAccessException(ex.toString());
     *    } finally {
     *        db.returnConnection(conn);
     *    }
     *  }
     */

    /**
     * the set of all users in the database
     */
    private Map<String, User> users;

    private Database db;

    /**
     * Create a new DAO for the users in the database
     */
    public UserDAO(Database database) {
        db = database;
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
        // if (users.containsKey(userToInsert.getUsername())) throw new DataAccessException("Error: already taken");
        // users.put(userToInsert.getUsername(), userToInsert);
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("""
                    INSERT INTO users (username, email, password)
                    VALUES (?, ?, ?);
                    """)) {
                preparedStatement.setString(1, userToInsert.getUsername());
                preparedStatement.setString(2, userToInsert.getEmail());
                preparedStatement.setString(3, userToInsert.getPassword());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: already taken");
        }
    }

    /**
     * find a user in the database based on their username
     * @param username the username of the user we are looking for
     * @return the user, if found; otherwise, null
     * @throws DataAccessException if there was a problem accessing the data
     */
    public User findUser(String username) throws DataAccessException {
        // return users.get(username);
        User user;
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("""
                    SELECT username, email, password
                    FROM users
                    WHERE username = ?
                    """)) {
                preparedStatement.setString(1, username);
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        user = new User(result.getString("username"),
                                result.getString("password"),
                                result.getString("email"));
                    } else {
                        user = null;
                    }
                } catch (SQLException e) {
                    throw new DataAccessException(e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return user;
    }

    /**
     * clear all users from the database
     * @throws DataAccessException if the users are not successfully removed from the database
     */
    public void clearUsers() throws DataAccessException {
        // users.clear();
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
