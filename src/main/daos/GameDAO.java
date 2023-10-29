package daos;

import chess.ChessGame;
import dataAccess.DataAccessException;
import models.Game;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class GameDAO {

    /**
     * the set of all games in the database
     */
    private Map<Integer, Game> games;

    /**
     * Create a new DAO to access the games in the database
     */
    public GameDAO() {

    }

    /**
     * Create a new DAO to access the games from a memory-based database
     * @param memoryGameTable a pointer to the memory-based table of games
     */
    public GameDAO(Map<Integer, Game> memoryGameTable) {
        games = memoryGameTable;
    }

    /**
     * insert a new game into the database
     * @param gameToInsert the game to be inserted into the database
     * @return the system-assigned game id by which this game will be found in the database
     * @throws DataAccessException if the game is not successfully added to the database
     */
    public int insertGame(Game gameToInsert) throws DataAccessException {
        // find the maximum game id and give this game the id one above that id before adding it to the db
        int gameID;
        if (games.isEmpty()) gameID = 1;
        else gameID = Collections.max(games.keySet()) + 1;
        gameToInsert.setGameID(gameID);
        games.put(gameID, gameToInsert);
        return gameID;
    }

    /**
     * find a game that already exists in the database, based on its id#
     * @param gameIdToFind the id # of the game to be found
     * @return the game, if found; otherwise, null
     * @throws DataAccessException there is a problem accessing the data
     */
    public Game findGame(int gameIdToFind) throws DataAccessException {
        return games.get(gameIdToFind);
    }

    /**
     * access all the games in the database
     * @return the set of all the games in the database
     * @throws DataAccessException if the data could not be successfully accessed
     */
    public Set<Game> findAllGames() throws DataAccessException {
        return new TreeSet<>(games.values());
    }

    /**
     * set one of the players in one of the games in the database to a given user
     * @param username the username of the user who is joining the game
     * @param gameId the game the user is joining
     * @param teamColor the team the user will be playing as in the game
     * @throws DataAccessException if the user is not successfully assigned as the desired player in the game
     */
    public void claimSpotInGame(String username, int gameId, ChessGame.TeamColor teamColor) throws DataAccessException {

    }

    /**
     * update the data for a game in the database
     * @param gameIdToUpdate the unique id # of the game to be updated
     * @param gameUpdated the new data to be assigned to the game
     * @throws DataAccessException if the game is not successfully updated
     */
    public void updateGame(int gameIdToUpdate, ChessGame gameUpdated) throws DataAccessException {

    }

    /**
     * remove a game from the database
     * @param gameIdToRemove the unique id # of the game to be removed
     * @throws DataAccessException if the game is not successfully removed
     */
    public void removeGame(int gameIdToRemove) throws DataAccessException {

    }

    /**
     * remove all the games from the database
     * @throws DataAccessException if all games are not successfully removed from the database
     */
    public void clearGames() throws DataAccessException {
        games.clear();
    }

}
