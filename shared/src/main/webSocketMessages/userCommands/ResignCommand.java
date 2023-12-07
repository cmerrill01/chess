package webSocketMessages.userCommands;

public class ResignCommand extends UserGameCommand {
    private final int gameID;
    private final CommandType commandType = CommandType.RESIGN;

    public ResignCommand(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
