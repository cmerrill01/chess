package daos;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.DataAccessException;
import dataAccess.Database;
import deserializers.ChessGameAdapter;
import chess.ChessGameImpl;
import models.Game;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class GameDAO {

    /**
     * An object for establishing connections to the MySQL database
     */
    private final Database db;

    /**
     * Create a new DAO to access the games in the database
     */
    public GameDAO(Database database) {
        db = database;
    }

    /**
     * insert a new game into the database
     * @param gameToInsert the game to be inserted into the database
     * @return the system-assigned game id by which this game will be found in the database
     * @throws DataAccessException if the game is not successfully added to the database
     */
    public int insertGame(Game gameToInsert) throws DataAccessException {
        int gameID = 0;
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("""
                    INSERT INTO games (game_name, game)
                    VALUES (?, ?);
                    """, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, gameToInsert.getGameName());
                preparedStatement.setString(2, new Gson().toJson(gameToInsert.getGame()));

                preparedStatement.executeUpdate();

                try (var result = preparedStatement.getGeneratedKeys()) {
                    if (result.next()) {
                        gameID = result.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return gameID;
    }

    /**
     * find a game that already exists in the database, based on its id#
     * @param gameIdToFind the id # of the game to be found
     * @return the game, if found; otherwise, null
     * @throws DataAccessException there is a problem accessing the data
     */
    public Game findGame(int gameIdToFind) throws DataAccessException {
        Game game;
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("""
                    SELECT *
                    FROM games
                    WHERE game_id = ?;
                    """)) {
                preparedStatement.setInt(1, gameIdToFind);
                try (var result = preparedStatement.executeQuery()) {
                    if (result.next()) {
                        game = new Game(result.getString("game_name"));
                        game.setGameID(result.getInt("game_id"));
                        game.setBlackUsername(result.getString("black_username"));
                        game.setWhiteUsername(result.getString("white_username"));
                        var builder = new GsonBuilder();
                        builder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
                        game.setGame(builder.create().fromJson(result.getString("game"), ChessGame.class));
                    } else {
                        game = null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return game;
    }

    /**
     * access all the games in the database
     * @return the set of all the games in the database
     * @throws DataAccessException if the data could not be successfully accessed
     */
    public Set<Game> findAllGames() throws DataAccessException {
        Set<Game> allGames = new TreeSet<>();
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM games;")) {
                try (var result = preparedStatement.executeQuery()) {
                    while (result.next()) {
                        Game game = new Game(result.getString("game_name"));
                        game.setGameID(result.getInt("game_id"));
                        game.setBlackUsername(result.getString("black_username"));
                        game.setWhiteUsername(result.getString("white_username"));
                        var builder = new GsonBuilder();
                        builder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
                        game.setGame(builder.create().fromJson(result.getString("game"), ChessGame.class));
                        allGames.add(game);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
        return allGames;
    }

    /**
     * set one of the players in one of the games in the database to a given user
     * @param username the username of the user who is joining the game
     * @param gameId the game the user is joining
     * @param teamColor the team the user will be playing as in the game
     * @throws DataAccessException if the user is not successfully assigned as the desired player in the game
     */
    public void claimSpotInGame(String username, int gameId, ChessGame.TeamColor teamColor) throws DataAccessException {
        try (Connection conn = db.getConnection()) {
            if (teamColor == ChessGame.TeamColor.WHITE) {
                try (var preparedQuery = conn.prepareStatement("""
                        SELECT white_username
                        FROM games
                        WHERE game_id = ?;
                        """)) {
                    preparedQuery.setInt(1, gameId);
                    try (var result = preparedQuery.executeQuery()) {
                        if (result.next()) {
                            if (result.getString("white_username") == null) {
                                try (var preparedUpdate = conn.prepareStatement("""
                                        UPDATE games
                                        SET white_username = ?
                                        WHERE game_id = ?;
                                        """)) {
                                    preparedUpdate.setString(1, username);
                                    preparedUpdate.setInt(2, gameId);

                                    preparedUpdate.executeUpdate();
                                }
                            } else {
                                throw new DataAccessException("Error: already taken");
                            }
                        }
                    }
                }
            } else if (teamColor == ChessGame.TeamColor.BLACK) {
                try (var preparedQuery = conn.prepareStatement("""
                        SELECT black_username
                        FROM games
                        WHERE game_id = ?;
                        """)) {
                    preparedQuery.setInt(1, gameId);
                    try (var result = preparedQuery.executeQuery()) {
                        if (result.next()) {
                            if (result.getString("black_username") == null) {
                                try (var preparedUpdate = conn.prepareStatement("""
                                        UPDATE games
                                        SET black_username = ?
                                        WHERE game_id = ?;
                                        """)) {
                                    preparedUpdate.setString(1, username);
                                    preparedUpdate.setInt(2, gameId);

                                    preparedUpdate.executeUpdate();
                                }
                            } else {
                                throw new DataAccessException("Error: already taken");
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * update the data for a game in the database
     * @param gameIdToUpdate the unique id # of the game to be updated
     * @param gameUpdated the new data to be assigned to the game
     * @throws DataAccessException if the game is not successfully updated
     */
    public void updateGame(int gameIdToUpdate, ChessGame gameUpdated) throws DataAccessException {
        if (gameUpdated == null) throw new DataAccessException("Error: please provide updated game");
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("""
                    UPDATE games
                    SET game = ?
                    WHERE game_id = ?;
                    """)) {
                preparedStatement.setString(1, new Gson().toJson(gameUpdated, ChessGameImpl.class));
                preparedStatement.setInt(2, gameIdToUpdate);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * remove a game from the database
     * @param gameIdToRemove the unique id # of the game to be removed
     * @throws DataAccessException if the game is not successfully removed
     */
    public void removeGame(int gameIdToRemove) throws DataAccessException {
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM games WHERE game_id = ?;")) {
                preparedStatement.setInt(1, gameIdToRemove);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * remove all the games from the database
     * @throws DataAccessException if all games are not successfully removed from the database
     */
    public void clearGames() throws DataAccessException {
        try (Connection conn = db.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games;")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

}
