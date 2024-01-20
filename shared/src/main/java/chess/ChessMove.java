package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    ChessPosition startPosition;
    ChessPosition endPosition;
    ChessPiece.PieceType promotionPiece;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;

    }

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        promotionPiece = null;
    }



    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public String toString()
    {
        return String.valueOf(endPosition);
       // return "Start Position: " + startPosition + " End Position: " + endPosition + " Promotion Piece: " + promotionPiece + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessMove chessMove = (ChessMove) o;
       // if(!Objects.equals(getPromotionPiece(), chessMove.getPromotionPiece())) return false;
       // System.out.println(Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition));
       return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && Objects.equals(getPromotionPiece(), chessMove.getPromotionPiece());
       // return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition);
    }

    @Override
    public int hashCode() {
       // if(promotionPiece != null) { return Objects.hash(startPosition, endPosition, promotionPiece);}
        return Objects.hash(startPosition, endPosition, promotionPiece);
    }


}




