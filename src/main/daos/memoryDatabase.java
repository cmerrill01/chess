package daos;

import models.*;
import java.util.Map;
import java.util.TreeMap;

public class memoryDatabase {

    private Map<String, AuthToken> authTokenTable;
    private Map<String, User> userTable;
    private Map<Integer, Game> gameTable;

    public memoryDatabase() {
        authTokenTable = new TreeMap<>();
        userTable = new TreeMap<>();
        gameTable = new TreeMap<>();
    }

    public Map<String, AuthToken> getAuthTokenTable() {
        return authTokenTable;
    }

    public Map<String, User> getUserTable() {
        return userTable;
    }

    public Map<Integer, Game> getGameTable() {
        return gameTable;
    }

}
