package responses;

import chess.ChessGame;
import models.Game;

import java.util.List;

public class ListGamesResponse {

    private static class gameRepresentation {
        private int gameID;
        private String whiteUsername;
        private String blackUsername;
        private String gameName;

        public gameRepresentation(Game game) {

        }
    }

    private List<gameRepresentation> games;
    private String message;

    public ListGamesResponse() {

    }

    public String getMessage() {
        return null;
    }

    public void setMessage(String message) {

    }

    public void addGame(ChessGame game) {

    }

}
