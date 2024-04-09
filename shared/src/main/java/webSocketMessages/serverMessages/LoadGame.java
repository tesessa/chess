package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage {
    private String game;
    public ServerMessageType serverMessageType = ServerMessageType.LOAD_GAME;


    public LoadGame(String game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

}
