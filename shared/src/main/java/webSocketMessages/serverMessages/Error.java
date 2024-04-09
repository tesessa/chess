package webSocketMessages.serverMessages;

public class Error extends ServerMessage {
    private String errorMessage;
    private ServerMessageType serverMessageType = ServerMessageType.ERROR;

    public Error(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = "Error " + errorMessage;
    }
}
