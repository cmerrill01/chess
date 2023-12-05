package webSocketMessages.userCommands;

public class LeaveCommand extends UserGameCommand {
    private final int gameId;
    private final CommandType commandType = CommandType.LEAVE;

    public LeaveCommand(String authToken, int gameId) {
        super(authToken);
        this.gameId = gameId;
    }
}
