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
    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
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
    public ChessGame.TeamColor getTeamColor() { return pieceColor; }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() { return type; }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> bishopMoves = new HashSet<ChessMove>();

        int row =  myPosition.getRow();
        int col = myPosition.getColumn();
        //go up one row and left one column
        for(int i = 0; i < 7; i++) {
            if(row > 1 && col > 1) {
                row--;
                col--;
                ChessPosition endPosition = new ChessPosition(row, col);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                if((board.getPiece(endPosition) != null) && board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
                bishopMoves.add(bishop);
                if(board.getPiece(endPosition) != null ) {
                    break;
                }
            }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        //go up one row and right one column
        for(int i = 0; i < 7; i++) {
            if(row > 1 && col < 8) {
                row--;
                col++;
                ChessPosition endPosition = new ChessPosition(row, col);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                if((board.getPiece(endPosition) != null) && board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
                bishopMoves.add(bishop);
                if(board.getPiece(endPosition) != null ) {
                    break;
                }
            }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        //go down one row and right one column
        for(int i = 0; i < 7; i++) {
            if(row < 8 && col > 1)  {
                row++;
                col--;
                ChessPosition endPosition = new ChessPosition(row, col);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                if((board.getPiece(endPosition) != null) && board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
                bishopMoves.add(bishop);
                if(board.getPiece(endPosition) != null ) {
                    break;
                }
            }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        //go down one row and left one column
        for(int i = 0; i < 7; i++) {
            if(col < 8 && row < 8) {
                row++;
                col++;
                ChessPosition endPosition = new ChessPosition(row, col);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                if((board.getPiece(endPosition) != null) && board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
                bishopMoves.add(bishop);
                if(board.getPiece(endPosition) != null ) {
                    break;
                }
            }
        }

       return bishopMoves;
    }
}
