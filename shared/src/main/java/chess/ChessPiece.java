package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> bishopMoves = new HashSet<ChessMove>();
       //ChessPosition endPosition = new ChessPosition(0,0);
        //ChessMove bishop; //= new ChessMove(myPosition, end);
       // bishopMoves.add(bishop);


        int row =  myPosition.getRow();
        int col = myPosition.getColumn();
        int rowul = row;
        int colul = col;
        int rowur = row;
        int colur = col;
        int rowdr = row;
        int coldr = col;
        int rowdl = row;
        int coldl = col;

        for(int i = 0; i<8; i++) {
            if(rowul > 1 && colul > 1) { /*this is to go up one row and left one column*/
                rowul --;
                colul --;
                ChessPosition endPosition = new ChessPosition(rowul, colul);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                bishopMoves.add(bishop);
            } if(rowur > 1 && colur < 8) { /*this is to go up one row and right one column*/
                rowur--;
                colur++;
                ChessPosition endPosition =  new ChessPosition(rowur, colur);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                bishopMoves.add(bishop);
            } if(rowdr < 8 && coldr < 8) {
                rowdr++;
                coldr++;
                ChessPosition endPosition = new ChessPosition(rowdr, coldr);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                bishopMoves.add(bishop);
            } if(rowdl < 8 && coldl > 1) {
                rowdl++;
                coldl--;
                ChessPosition endPosition = new ChessPosition(rowdl, coldl);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                bishopMoves.add(bishop);
            }
        }

       return bishopMoves;
    }
}
