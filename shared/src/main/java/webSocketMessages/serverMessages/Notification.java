package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    private String message;
    private ServerMessageType serverMessageType = ServerMessageType.NOTIFICATION;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
}