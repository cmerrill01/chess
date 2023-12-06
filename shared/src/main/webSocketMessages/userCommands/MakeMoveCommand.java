package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final int gameId;
    private final ChessMove move;
    private final CommandType commandType = CommandType.MAKE_MOVE;

    public MakeMoveCommand(String authToken, int gameId, ChessMove move) {
        super(authToken);
        this.gameId = gameId;
        this.move = move;
    }

    public int getGameId() {
        return gameId;
    }

    public ChessMove getMove() {
        return move;
    }
}
