package server;

import chess.ChessGame;
import models.AuthToken;
import models.Game;

import java.util.List;

public class ServerFacade {

    public ServerFacade(String url) {

    }

    public void clear() {

    }

    public AuthToken register(String username, String password, String email) {
        return null;
    }

    public AuthToken login(String username, String password) {
        return null;
    }

    public void logout(String authToken) {

    }

    public List<Game> listGames(String authToken) {
        return null;
    }

    public int createGame(String gameName) {
        return 0;
    }

    public void joinGame(ChessGame.TeamColor playerColor, int gameId) {

    }

}
