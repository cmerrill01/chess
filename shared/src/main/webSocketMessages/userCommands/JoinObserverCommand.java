package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand {
    private final CommandType commandType = CommandType.JOIN_OBSERVER;
    private final int gameId;

    public JoinObserverCommand(String authToken, int gameId) {
        super(authToken);
        this.gameId = gameId;
    }
}
