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
     * This method is to get all the positions the king can make
     * I added another method to simplify this one and to make sure that it doesn't have a lot of repeating code
     * the method is called endKingPosition
     */
    public void kingPiece(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
            endPosition(moves, row + 1, col + 1, myPosition, board);
            endPosition(moves, row + 1, col, myPosition, board);
            endPosition(moves, row, col + 1, myPosition, board);
            endPosition(moves, row + 1, col - 1, myPosition, board);
            endPosition(moves, row - 1, col + 1, myPosition, board);
            endPosition(moves, row - 1, col, myPosition, board);
            endPosition(moves, row - 1, col - 1, myPosition, board);
            endPosition(moves, row, col - 1, myPosition, board);
    }

    /**
     * this is for the Knight piece, it will find the moves it can make
     */
    public void knightPiece(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        row++;
        col = col-2;
        endPosition(moves, row, col, myPosition, board);
        col = col + 4;
        endPosition(moves, row, col, myPosition, board);
        row = row -2;
        endPosition(moves, row, col, myPosition, board);
        col = col - 4;
        endPosition(moves, row, col, myPosition, board);
        row--;
        col++;
        endPosition(moves, row, col, myPosition, board);
        col = col +2;
        endPosition(moves, row, col, myPosition, board);
        row = row +4;
        endPosition(moves, row, col, myPosition, board);
        col = col -2;
        endPosition(moves, row, col, myPosition, board);
    }

    /**
     * this is the method for the pawn piece
     */
    public void pawnPiece(Collection<ChessMove> moves, ChessBoard board, ChessPosition myPosition) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPiece piece = board.getPiece(myPosition);
        ChessPosition opponent1 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()+1);
        ChessPosition opponent2 = new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()-1);
        ChessPosition opponent3 = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()-1);
        ChessPosition opponent4 = new ChessPosition(myPosition.getRow()-1, myPosition.getColumn()+1);
        if(piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            if(row == 2) {
                if(board.getPiece(opponent1) != null && board.getPiece(opponent1).getTeamColor()!= board.getPiece(myPosition).getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, opponent1, board.getPiece(myPosition).getPieceType());
                    moves.add(move);
                }
                if(board.getPiece(opponent2) != null && board.getPiece(opponent2).getTeamColor()!= board.getPiece(myPosition).getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, opponent2, board.getPiece(myPosition).getPieceType());
                    moves.add(move);
                }
                row++;
                endPosition(moves, row, col, myPosition, board);
                row++;
                endPosition(moves, row, col, myPosition, board);
            } else {
                if(board.getPiece(opponent1) != null && board.getPiece(opponent1).getTeamColor()!= board.getPiece(myPosition).getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, opponent1, board.getPiece(myPosition).getPieceType());
                    moves.add(move);
                }
                if(board.getPiece(opponent2) != null && board.getPiece(opponent2).getTeamColor()!= board.getPiece(myPosition).getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, opponent2, board.getPiece(myPosition).getPieceType());
                    moves.add(move);
                }
                row++;
                endPosition(moves, row, col, myPosition, board);
            }
        } else if(piece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
            if(row == 7) {
                if(board.getPiece(opponent3) != null && board.getPiece(opponent3).getTeamColor()!= board.getPiece(myPosition).getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, opponent3, board.getPiece(myPosition).getPieceType());
                    moves.add(move);
                }
                if(board.getPiece(opponent4) != null && board.getPiece(opponent4).getTeamColor()!= board.getPiece(myPosition).getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, opponent4, board.getPiece(myPosition).getPieceType());
                    moves.add(move);
                }
                row--;
                endPosition(moves, row, col, myPosition, board);
                row--;
                endPosition(moves, row, col, myPosition, board);
            } else {
                if(board.getPiece(opponent3) != null && board.getPiece(opponent3).getTeamColor()!= board.getPiece(myPosition).getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, opponent3, board.getPiece(myPosition).getPieceType());
                    moves.add(move);
                }
                if(board.getPiece(opponent4) != null && board.getPiece(opponent4).getTeamColor()!= board.getPiece(myPosition).getTeamColor()) {
                    ChessMove move = new ChessMove(myPosition, opponent4, board.getPiece(myPosition).getPieceType());
                    moves.add(move);
                }
                row--;
                endPosition(moves, row, col, myPosition, board);
            }
        }
    }

    /**
     * I am making this method to simplify the code of the pieceMoves method
     * we will test it out and make sure it works
     * it should take the row, col, start position, piecetype, and collection
     * and make a position object and add it to the collection
     */
    public Collection<ChessMove> endPosition(Collection<ChessMove> moves, int row, int col, ChessPosition startPosition, ChessBoard board) {
        if(row <=8 && row > 0 && col <= 8 && col > 0) {
            ChessPosition endPosition = new ChessPosition(row, col);
            ChessMove piece = new ChessMove(startPosition, endPosition, board.getPiece(startPosition).getPieceType());
            if ((board.getPiece(endPosition) != null) && board.getPiece(endPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                moves.add(piece);
            } else if (board.getPiece(endPosition) == null) {
                //System.out.println("{" + endPosition.getRow() + "," + endPosition.getColumn() + "}");
                moves.add(piece);
            }
        }
        return moves;
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
        ChessPiece piece = board.getPiece(myPosition);
       if(piece.getPieceType().equals(PieceType.KING)) {
           kingPiece(moves, board, myPosition);
       }

       if(piece.getPieceType().equals(PieceType.BISHOP)) {
            bishopMoves(moves, board, myPosition);
        }

       if(piece.getPieceType().equals(PieceType.KNIGHT)) {
            knightPiece(moves, board, myPosition);
       }

       if(piece.getPieceType().equals(PieceType.PAWN)) {
           pawnPiece(moves, board, myPosition);
       }
        return moves;
    }

    public String toString() {
        return String.valueOf(getPieceType());
    }
}
