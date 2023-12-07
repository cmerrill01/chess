package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private final int gameID;
    private final CommandType commandType = CommandType.LEAVE;

    public LeaveCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
