package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    private ChessGame game;
    public ServerMessageType serverMessageType = ServerMessageType.LOAD_GAME;


    public LoadGame(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public String toString() {
        return "Load Game";
    }

}
