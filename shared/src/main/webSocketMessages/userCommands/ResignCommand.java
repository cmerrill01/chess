package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private final int gameId;
    private final CommandType commandType = CommandType.RESIGN;

    public ResignCommand(String authToken, int gameId) {
        super(authToken);
        this.gameId = gameId;
    }
}
