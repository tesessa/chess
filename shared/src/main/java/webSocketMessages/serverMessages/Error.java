package webSocketMessages.serverMessages;

public class Error extends ServerMessage {
    private String errorMessage;
    public ServerMessageType type = ServerMessageType.ERROR;

    public Error(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = "error " + errorMessage;
    }

    public String toString() {
        return errorMessage;
    }
}
