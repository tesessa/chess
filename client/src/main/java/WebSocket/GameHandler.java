package WebSocket;

import chess.ChessBoard;
import chess.ChessGame;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.LoadGame;

public class GameHandler {

    ChessGame game = new ChessGame();
    public void printMessage(String message) {
       System.out.println(message);
    }

    public void updateGame(LoadGame load) {
        game = load.getGame();
    }

    public ChessGame getGame() {
        return game;
    }
    public void drawBoard(ChessGame.TeamColor playerColor) {
        EscapeSequences draw = new EscapeSequences();
        ChessBoard board = game.getBoard();
        if(playerColor == ChessGame.TeamColor.WHITE) {
            draw.printWhiteBoard(board);
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            draw.printBlackBoard(board);
        } else {
            draw.printWhiteBoard(board);
        }
    }
}
