import ExceptionClasses.ResponseException;
import chess.*;
import ui.PreLoginRepl;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if(args.length == 1) {
            serverUrl = args[0];
        }

        try {
            new PreLoginRepl(serverUrl).run();
        } catch(ResponseException ex) {

        }

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
    }
}