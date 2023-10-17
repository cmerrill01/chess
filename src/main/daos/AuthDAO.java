package daos;

import dataAccess.DataAccessException;
import models.AuthToken;
import java.util.Set;

public class AuthDAO {

    private Set<AuthToken> authTokens;

    public AuthDAO() {

    }

    public void insertAuthToken(AuthToken tokenToInsert) throws DataAccessException {

    }


    public AuthToken findAuthToken(String username) throws DataAccessException {
        return null;
    }



    public void removeAuthToken(String username) throws DataAccessException {

    }


    public void clearAuthTokens() throws DataAccessException {

    }

}
