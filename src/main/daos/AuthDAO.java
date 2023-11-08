package daos;

import dataAccess.DataAccessException;
import dataAccess.Database;
import models.AuthToken;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthDAO {

    /**
     * An object for creating connections to the MySQL database
     */
    private final Database db;

    /**
     * Create a new DAO to access the set of authentication tokens
     */
    public AuthDAO(Database database) {
        db = database;
    }

    /**
     * insert an authentication token into the database
     * @param tokenToInsert the token to be added to the database
     * @throws DataAccessException if the user already has an authentication token in the database
     */
    public void insertAuthToken(AuthToken tokenToInsert) throws DataAccessException {
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("""
                    INSERT INTO auth_tokens (auth_token, username)
                    VALUES (?, ?);
                    """)) {
                preparedStatement.setString(1, tokenToInsert.authToken());
                preparedStatement.setString(2, tokenToInsert.username());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: already logged in");
        }

    }

    /**
     * find whether an authentication token belonging to a given user is in the database
     * @param authToken the username of the user whose authentication token we are looking for
     * @return the authentication token of the given user, if found; otherwise, null
     * @throws DataAccessException if there is a problem with accessing the data
     */
    public AuthToken findAuthToken(String authToken) throws DataAccessException {
        AuthToken token;
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("""
                    SELECT auth_token, username
                    FROM auth_tokens
                    WHERE auth_token = ?
                    """)) {
                preparedStatement.setString(1, authToken);
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        token = new AuthToken(result.getString("username"),
                                result.getString("auth_token"));
                    } else {
                        token = null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return token;
    }

    /**
     * remove a user's authentication token from the database
     * @param authToken the authentication token of the user whose authentication token is to be removed
     * @throws DataAccessException if the removal was unsuccessful
     */
    public void removeAuthToken(String authToken) throws DataAccessException {
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("""
                    DELETE FROM auth_tokens
                    WHERE auth_token = ?
                    """)) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * clear all authentication tokens from the database
     * @throws DataAccessException if the database was not successfully cleared
     */
    public void clearAuthTokens() throws DataAccessException {
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth_tokens")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
