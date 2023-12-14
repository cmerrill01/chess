package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import daos.AuthDAO;
import daos.GameDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import deserializers.ChessMoveAdapter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadGameMessage;
import webSocketMessages.serverMessages.NotificationMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Database db;

    public WebSocketHandler(Database database) {
        db = database;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        if (message.toLowerCase().contains("join_player")) {
            var command = new Gson().fromJson(message, JoinPlayerCommand.class);
            joinPlayer(command.getAuthString(), command.getGameID(), command.getPlayerColor(), session);
        } else if (message.toLowerCase().contains("join_observer")) {
            var command = new Gson().fromJson(message, JoinObserverCommand.class);
            joinObserver(command.getAuthString(), command.getGameID(), session);
        } else if (message.toLowerCase().contains("make_move")) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ChessMove.class, new ChessMoveAdapter());
            var command = builder.create().fromJson(message, MakeMoveCommand.class);
            makeMove(command.getAuthString(), command.getGameID(), command.getMove());
        } else if (message.toLowerCase().contains("leave")) {
            var command = new Gson().fromJson(message, LeaveCommand.class);
            leave(command.getAuthString(), command.getGameID());
        } else if (message.toLowerCase().contains("resign")) {
            var command = new Gson().fromJson(message, ResignCommand.class);
            resign(command.getAuthString(), command.getGameID());
        }
    }

    private void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor, Session session) throws IOException {
        GameDAO gameAccess = new GameDAO(db);
        AuthDAO authAccess = new AuthDAO(db);

        try {
            connections.add(authToken, gameID, playerColor, session);
        } catch (IOException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
            return;
        }

        try {
            if (invalidGameId(authToken, gameID, gameAccess)) return;
            if (invalidAuthToken(authToken, authAccess)) return;
            if (spotNotReserved(authToken, gameID, playerColor, gameAccess, authAccess)) return;
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
            return;
        }

        try {
            ChessGame game = gameAccess.findGame(gameID).getGame();
            LoadGameMessage messageToRoot = new LoadGameMessage(game);
            connections.send(authToken, messageToRoot);

            String username = authAccess.findAuthToken(authToken).username();
            String othersMessageString = String.format("%s joined the game as the %s player.", username, playerColor.toString().toLowerCase());
            NotificationMessage messageToOthers = new NotificationMessage(othersMessageString);
            connections.broadcast(authToken, gameID, messageToOthers);
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
        }
    }

    private void joinObserver(String authToken, int gameID, Session session) throws IOException {
        GameDAO gameAccess = new GameDAO(db);
        AuthDAO authAccess = new AuthDAO(db);

        try {
            connections.add(authToken, gameID, null, session);
        } catch (IOException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
            return;
        }

        try {
            if (invalidGameId(authToken, gameID, gameAccess)) return;
            if (invalidAuthToken(authToken, authAccess)) return;
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
            return;
        }

        try {
            ChessGame game = gameAccess.findGame(gameID).getGame();
            LoadGameMessage messageToRoot = new LoadGameMessage(game);
            connections.send(authToken, messageToRoot);

            String username = authAccess.findAuthToken(authToken).username();
            String othersMessageString = String.format("%s joined the game as an observer.", username);
            NotificationMessage messageToOthers = new NotificationMessage(othersMessageString);
            connections.broadcast(authToken, gameID, messageToOthers);
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(new Gson().toJson(messageToRoot));
        }

    }

    private void makeMove(String authToken, int gameID, ChessMove move) throws IOException {
        GameDAO gameAccess = new GameDAO(db);
        AuthDAO authAccess = new AuthDAO(db);

        try {
            if (invalidGameId(authToken, gameID, gameAccess)) return;
            if (invalidAuthToken(authToken, authAccess)) return;
            if (gameOver(authToken, gameID, gameAccess)) return;
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
            return;
        }

        try {
            // Get the game
            ChessGame game = gameAccess.findGame(gameID).getGame();
            // Ensure it's this players turn before allowing them to make a move
            if (game.getTeamTurn() == connections.getPlayerColor(authToken)) {
                // Make the move
                game.makeMove(move);
                // Update the game
                gameAccess.updateGame(gameID, game);
                // Load the board for all players
                LoadGameMessage messageToAll = new LoadGameMessage(game);
                connections.broadcast(null, gameID, messageToAll);
                // Notify other players that a move was made
                NotificationMessage messageToOthers = new NotificationMessage(String.format(
                        "%s has made a move: %s", authAccess.findAuthToken(authToken).username(), move.toString())
                );
                connections.broadcast(authToken, gameID, messageToOthers);
                if (game.getStatus() != ChessGame.GameStatus.UNDECIDED) {
                    sendGameOverNotification(gameID, game, gameAccess);
                } else {
                    ChessGame.TeamColor opponentColor = switch (connections.getPlayerColor(authToken)) {
                        case WHITE -> ChessGame.TeamColor.BLACK;
                        case BLACK -> ChessGame.TeamColor.WHITE;
                    };
                    if (game.isInCheck(opponentColor)) {
                        sendCheckNotification(gameID, opponentColor, gameAccess);
                    }
                }
            } else {
                ErrorMessage messageToRoot = new ErrorMessage("Error: Illegal move attempt; not this user's turn.");
                connections.send(authToken, messageToRoot);
            }
        } catch (DataAccessException | InvalidMoveException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
        }
    }

    private void leave(String authToken, int gameID) throws IOException {
        GameDAO gameAccess = new GameDAO(db);
        AuthDAO authAccess = new AuthDAO(db);

        try {
            if (invalidGameId(authToken, gameID, gameAccess)) return;
            if (invalidAuthToken(authToken, authAccess)) return;
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
            return;
        }

        try {
            String username = authAccess.findAuthToken(authToken).username();
            // Game is updated in the database.
            gameAccess.leaveGame(username, gameID, connections.getPlayerColor(authToken));
            // Game is updated to remove the root client.
            connections.remove(authToken);
            // Server sends a Notification message to all other clients in that game informing them that the root client left. This applies to both players and observers.
            NotificationMessage messageToOthers = new NotificationMessage(String.format("%s left the game.", username));
            connections.broadcast(null, gameID, messageToOthers);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private void resign(String authToken, int gameID) throws IOException {
        GameDAO gameAccess = new GameDAO(db);
        AuthDAO authAccess = new AuthDAO(db);

        try {
            if (invalidGameId(authToken, gameID, gameAccess)) return;
            if (invalidAuthToken(authToken, authAccess)) return;
            if (playerIsObserver(authToken)) return;
            if (gameOver(authToken, gameID, gameAccess)) return;
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
            return;
        }

        try {
            // Get the game
            ChessGame game = gameAccess.findGame(gameID).getGame();
            // Resign the player
            game.resignPlayer(connections.getPlayerColor(authToken));
            // Update the game
            gameAccess.updateGame(gameID, game);
            // Notify all players
            NotificationMessage messageToAll = new NotificationMessage(String.format(
                    "%s has resigned.", authAccess.findAuthToken(authToken).username()
            ));
            connections.broadcast(null, gameID, messageToAll);
        } catch (DataAccessException e) {
            ErrorMessage messageToRoot = new ErrorMessage(e.getMessage());
            connections.send(authToken, messageToRoot);
        }
    }

    private boolean spotNotReserved(String authToken, int gameID, ChessGame.TeamColor playerColor, GameDAO gameAccess, AuthDAO authAccess) throws DataAccessException, IOException {
        if (playerColor == ChessGame.TeamColor.WHITE) {
            if (!Objects.equals(gameAccess.findGame(gameID).getWhiteUsername(), authAccess.findAuthToken(authToken).username())) {
                ErrorMessage messageToRoot = new ErrorMessage("Error: The white spot has not been reserved for this player.");
                connections.send(authToken, messageToRoot);
                return true;
            }
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            if (!Objects.equals(gameAccess.findGame(gameID).getBlackUsername(), authAccess.findAuthToken(authToken).username())) {
                ErrorMessage messageToRoot = new ErrorMessage("Error: The black spot has not been reserved for this player.");
                connections.send(authToken, messageToRoot);
                return true;
            }
        }
        return false;
    }

    private boolean invalidAuthToken(String authToken, AuthDAO authAccess) throws DataAccessException, IOException {
        if (authAccess.findAuthToken(authToken) == null) {
            ErrorMessage messageToRoot = new ErrorMessage("Error: The user does not have a valid authentication token.");
            connections.send(authToken, messageToRoot);
            return true;
        }
        return false;
    }

    private boolean invalidGameId(String authToken, int gameID, GameDAO gameAccess) throws DataAccessException, IOException {
        if (gameAccess.findGame(gameID) == null) {
            ErrorMessage messageToRoot = new ErrorMessage("Error: The game the user requested to join does not exist.");
            connections.send(authToken, messageToRoot);
            return true;
        }
        return false;
    }

    private boolean gameOver(String authToken, int gameID, GameDAO gameAccess) throws DataAccessException, IOException {
        if (gameAccess.findGame(gameID).getGame().getStatus() != ChessGame.GameStatus.UNDECIDED) {
            ErrorMessage messageToRoot = new ErrorMessage(String.format(
                    "Error: This game is over and no moves can be made.\nGame status: %s", 
                    gameAccess.findGame(gameID).getGame().getStatus())
            );
            connections.send(authToken, messageToRoot);
            return true;
        }
        return false;
    }

    private boolean playerIsObserver(String authToken) throws IOException {
        if (connections.getPlayerColor(authToken) == null) {
            ErrorMessage messageToRoot = new ErrorMessage("Error: This user is an observer and cannot perform this action.");
            connections.send(authToken, messageToRoot);
            return true;
        }
        return false;
    }


    private void sendCheckNotification(int gameID, ChessGame.TeamColor opponentColor, GameDAO gameAccess) throws DataAccessException, IOException {
        NotificationMessage inCheckMessage;
        switch (opponentColor) {
            case WHITE -> inCheckMessage = new NotificationMessage(String.format(
                    "%s is in check.",
                    gameAccess.findGame(gameID).getWhiteUsername()
            ));
            case BLACK -> inCheckMessage = new NotificationMessage(String.format(
                    "%s is in check.",
                    gameAccess.findGame(gameID).getBlackUsername()
            ));
            default -> throw new IllegalStateException("Unexpected player color: " + opponentColor);
        }
        connections.broadcast(null, gameID, inCheckMessage);
    }

    private void sendGameOverNotification(int gameID, ChessGame game, GameDAO gameAccess) throws DataAccessException, IOException {
        NotificationMessage gameOverMessage;
        switch (game.getStatus()) {
            case DRAW -> gameOverMessage = new NotificationMessage("Stalemate.\nGame over.");
            case BLACK_VICTORY -> gameOverMessage = new NotificationMessage(String.format(
                    "%s is in checkmate.\nGame over: %s wins!",
                    gameAccess.findGame(gameID).getWhiteUsername(),
                    gameAccess.findGame(gameID).getBlackUsername()
            ));
            case WHITE_VICTORY -> gameOverMessage = new NotificationMessage(String.format(
                    "%s is in checkmate.\nGame over: %s wins!",
                    gameAccess.findGame(gameID).getBlackUsername(),
                    gameAccess.findGame(gameID).getWhiteUsername()
            ));
            default -> throw new IllegalStateException("Unexpected game status: " + game.getStatus());
        }
        connections.broadcast(null, gameID, gameOverMessage);
    }

}
