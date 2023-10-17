package daos;

import dataAccess.DataAccessException;
import models.User;
import java.util.Set;

public class UserDAO {

    private Set<User> users;

    public UserDAO() {

    }

    public void insertUser(User userToInsert) throws DataAccessException {

    }

    public User findUser(String username) throws DataAccessException {
        return null;
    }

    public void changePassword(String username) throws DataAccessException {

    }

    public void removeUser(String username) throws DataAccessException {

    }

}
