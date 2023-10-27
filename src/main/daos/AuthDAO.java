package daos;

import dataAccess.DataAccessException;
import models.AuthToken;

import java.util.*;

public class AuthDAO {

    /**
     * the set of all authentication tokens for currently logged-in users
     */
    private Map<String, AuthToken> authTokens;

    /**
     * Create a new DAO to access the set of authentication tokens
     */
    public AuthDAO() {

    }

    /**
     * Create a new DAO to access the set of authentication tokens
     * @param memoryAuthTokenTable a pointer to the memory-based table of authentication tokens to be accessed
     */
    public AuthDAO(Map<String, AuthToken> memoryAuthTokenTable) {
        authTokens = memoryAuthTokenTable;
    }

    /**
     * insert an authentication token into the database
     * @param tokenToInsert the token to be added to the database
     * @throws DataAccessException if the user already has an authentication token in the database
     */
    public void insertAuthToken(AuthToken tokenToInsert) throws DataAccessException {
        if (authTokens.containsKey(tokenToInsert.getUsername())) throw new DataAccessException("Error: already logged in");
        authTokens.put(tokenToInsert.getUsername(), tokenToInsert);
    }

    /**
     * find whether an authentication token belonging to a given user is in the database
     * @param username the username of the user whose authentication token we are looking for
     * @return the authentication token of the given user, if found; otherwise, null
     * @throws DataAccessException if there is a problem with accessing the data
     */
    public AuthToken findAuthToken(String username) throws DataAccessException {
        return authTokens.get(username);
    }

    /**
     * remove a user's authentication token from the database
     * @param authToken the authentication token of the user whose authentication token is to be removed
     * @throws DataAccessException if the removal was unsuccessful
     */
    public void removeAuthToken(String authToken) throws DataAccessException {
        boolean removedToken = false;
        for (String username : authTokens.keySet()) {
            if (Objects.equals(authTokens.get(username).getAuthToken(), authToken)) {
                authTokens.remove(username);
                removedToken = true;
                break;
            }
        }
        if (!removedToken) throw new DataAccessException("Error: unauthorized");
    }

    /**
     * clear all authentication tokens from the database
     * @throws DataAccessException if the database was not successfully cleared
     */
    public void clearAuthTokens() throws DataAccessException {
        authTokens.clear();
    }

}
