package WebSocket;

import chess.ChessBoard;
import chess.ChessGame;
import ui.EscapeSequences;
import webSocketMessages.serverMessages.LoadGame;

public class GameHandler {

    ChessGame game = new ChessGame();

    public void updateGame(ChessGame newGame) {
       // System.out.println("Load game");
        game =  newGame;
      //  drawBoard(color);
    }

    public ChessGame getGame() {
        return game;
    }
    public void drawBoard(ChessGame.TeamColor playerColor) {
        int [][] arr = new int[8][8];
        EscapeSequences draw = new EscapeSequences();
        ChessBoard board = game.getBoard();
        if(playerColor == ChessGame.TeamColor.WHITE) {
            draw.printWhiteBoard(board, arr);
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            draw.printBlackBoard(board, arr);
        } else {
            draw.printWhiteBoard(board, arr);
        }
    }
}
