package chess;

import java.util.Arrays;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;

       // throw new RuntimeException("Not implemented");
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();
        return squares[row-1][col-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        ChessPiece rookW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        ChessPiece knightW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        ChessPiece bishopW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        ChessPiece queenW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        ChessPiece kingW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        ChessPiece pawnW = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece rookB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        ChessPiece knightB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        ChessPiece bishopB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        ChessPiece queenB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        ChessPiece kingB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        ChessPiece pawnB = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        for(int row = 1; row< 3; row++) {
            for(int col = 1; col < 9; col++) {
                ChessPosition piece = new ChessPosition(row, col);
                if(row == 1) {
                   // ChessPosition piece = new ChessPosition(row, col);
                    if(col == 1 || col == 8) {
                        addPiece(piece, rookW);
                    } else if(col == 2 || col == 7) {
                        addPiece(piece, knightW);
                    } else if (col == 3 || col == 6) {
                        addPiece(piece, bishopW);
                    } else if (col == 4) {
                        addPiece(piece, queenW);
                    } else if (col == 5) {
                        addPiece(piece, kingW);
                    }
                }
                if(row == 2) {
                    addPiece(piece, pawnW);
                }
            }
        }
        for(int row = 8; row>6; row--) {
            for(int col = 1; col < 9; col++) {
                ChessPosition piece = new ChessPosition(row, col);
                if(row == 8) {
                    // ChessPosition piece = new ChessPosition(row, col);
                    if(col == 1 || col == 8) {
                        addPiece(piece, rookB);
                    } else if(col == 2 || col == 7) {
                        addPiece(piece, knightB);
                    } else if (col == 3 || col == 6) {
                        addPiece(piece, bishopB);
                    } else if (col == 4) {
                        addPiece(piece, queenB);
                    } else if (col == 5) {
                        addPiece(piece, kingB);
                    }
                }
                if(row == 7) {
                    addPiece(piece, pawnB);
                }
            }
        }
    }

    /*public String toString() {
        return Arrays.deepToString(squares);
    }*/

    @Override
    public String toString() {
        return "ChessBoard{" + Arrays.deepToString(squares) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Arrays.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}




