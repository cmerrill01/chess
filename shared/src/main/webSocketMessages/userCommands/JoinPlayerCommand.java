package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    private final int gameId;
    private final ChessGame.TeamColor playerColor;
    private final CommandType commandType = CommandType.JOIN_PLAYER;

    public JoinPlayerCommand(String authToken, int gameId, ChessGame.TeamColor playerColor) {
        super(authToken);
        this.gameId = gameId;
        this.playerColor = playerColor;
    }
}
