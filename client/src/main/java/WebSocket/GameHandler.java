package WebSocket;

import chess.ChessGame;
import webSocketMessages.serverMessages.ServerMessage;

public interface GameHandler {

    public void updateGame(ChessGame game);
    public void printMessage(String message, ServerMessage s);

 //   void notify(String message);
}
