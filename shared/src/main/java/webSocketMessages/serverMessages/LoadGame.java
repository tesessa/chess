package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    private ChessGame game;
    public ServerMessageType type = ServerMessageType.LOAD_GAME;


    public LoadGame(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    public String toString() {
        return "Load Game";
    }

}
