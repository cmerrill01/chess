package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    private final String errorMessage;
    public ErrorMessage(String message) {
        super(ServerMessageType.ERROR);
        errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
