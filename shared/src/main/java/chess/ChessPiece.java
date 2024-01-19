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

    public void bishopMoves(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        for (int i = 0; i < 7; i++) {
            if (row > 1 && col > 1) {
                row--;
                col--;
                ChessPosition endPosition = new ChessPosition(row, col);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                if ((board.getPiece(endPosition) != null) && board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
                moves.add(bishop);
                if (board.getPiece(endPosition) != null) {
                    break;
                }
            }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        //go up one row and right one column
        for (int i = 0; i < 7; i++) {
            if (row > 1 && col < 8) {
                row--;
                col++;
                ChessPosition endPosition = new ChessPosition(row, col);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                ChessPiece piece = board.getPiece(endPosition);
                ChessPiece start = board.getPiece(myPosition);
                if ((piece != null) && piece.getTeamColor() == start.getTeamColor()) {
                    break;
                }
                moves.add(bishop);
                if (board.getPiece(endPosition) != null) {
                    break;
                }
            }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        //go down one row and right one column
        for (int i = 0; i < 7; i++) {
            if (row < 8 && col > 1) {
                row++;
                col--;
                ChessPosition endPosition = new ChessPosition(row, col);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                if ((board.getPiece(endPosition) != null) && board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
                moves.add(bishop);
                if (board.getPiece(endPosition) != null) {
                    break;
                }
            }
        }
        row = myPosition.getRow();
        col = myPosition.getColumn();
        //go down one row and left one column
        for (int i = 0; i < 7; i++) {
            if (col < 8 && row < 8) {
                row++;
                col++;
                ChessPosition endPosition = new ChessPosition(row, col);
                ChessMove bishop = new ChessMove(myPosition, endPosition, PieceType.BISHOP);
                if ((board.getPiece(endPosition) != null) && board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()) {
                    break;
                }
                moves.add(bishop);
                if (board.getPiece(endPosition) != null) {
                    break;
                }
            }
        }
    }

    /**
     * I am making this method to simplify the code of the pieceMoves method
     * we will test it out and make sure it works
     * it should take the row, col, start position, piecetype, and collection
     * and make a position object and add it to the collection
     */
    public Collection<ChessMove> endKingPosition(Collection<ChessMove> moves, int row, int col, ChessPosition startPosition, ChessBoard board) {
        if(row <=8 && row > 0 && col <= 8 && col > 0) {
            ChessPosition endPosition = new ChessPosition(row, col);
            ChessMove piece = new ChessMove(startPosition, endPosition, board.getPiece(startPosition).getPieceType());
            if ((board.getPiece(endPosition) != null) && board.getPiece(endPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                moves.add(piece);
            } else if (board.getPiece(endPosition) == null) {
                moves.add(piece);
            }
        }
        return moves;
    }


    /**
     * This method is to get all the positions the king can make
     * I added another method to simplify this one and to make sure that it doesn't have a lot of repeating code
     * the method is called endKingPosition
     */
    public void kingPiece(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if(row<8 && col<8) {
            endKingPosition(moves, row + 1, col + 1, myPosition, board);
        } if(row<8) {
            endKingPosition(moves, row + 1, col, myPosition, board);
        } if(col < 8) {
            endKingPosition(moves, row, col + 1, myPosition, board);
        } if(row < 8 && col > 0) {
            endKingPosition(moves, row + 1, col - 1, myPosition, board);
        }  if(row > 0 && col < 8) {
            endKingPosition(moves, row - 1, col + 1, myPosition, board);
        } if(row > 0) {
            endKingPosition(moves, row - 1, col, myPosition, board);
        } if(row > 0 && col > 0) {
            endKingPosition(moves, row - 1, col - 1, myPosition, board);
        } if (col > 0) {
            endKingPosition(moves, row, col - 1, myPosition, board);
        }
    }



    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        System.out.println(board.getPiece(myPosition));
        int row =  myPosition.getRow();
        int col = myPosition.getColumn();
       if(board.getPiece(myPosition).getPieceType().equals(PieceType.KING)) {
           kingPiece(moves, board, myPosition);
       }

        if(board.getPiece(myPosition).getPieceType().equals(PieceType.BISHOP)) {
            bishopMoves(moves, board, myPosition);
        }
        return moves;
    }

    public String toString() {
        return String.valueOf(getPieceType());
    }
}
