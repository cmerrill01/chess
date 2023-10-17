package daos;

import chess.ChessGame;
import dataAccess.DataAccessException;
import models.Game;
import java.util.Set;

public class GameDAO {

    private Set<Game> games;

    public GameDAO() {

    }

    public void insertGame(Game gameToInsert) throws DataAccessException {

    }

    public Game findGame(int gameIdToFind) throws DataAccessException {
        return null;
    }

    public Set<Game> findAllGames() throws DataAccessException {
        return null;
    }

    public void claimSpotInGame(String username, int gameId, ChessGame.TeamColor teamColor) throws DataAccessException {

    }

    public void updateGame(int gameIdToUpdate, Game gameUpdated) throws DataAccessException {

    }

    public void removeGame(int gameIdToRemove) throws DataAccessException {

    }

    public void clearGames() throws DataAccessException {

    }

}
