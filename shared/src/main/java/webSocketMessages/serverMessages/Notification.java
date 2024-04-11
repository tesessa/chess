package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    private String message;
    private ServerMessageType type = ServerMessageType.NOTIFICATION;

    public Notification(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }

    public String toString() {
        return message;
    }
}
