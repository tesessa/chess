package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard gameBoard = new ChessBoard();
    private int whiteTeamTurn = 0;
    private int blackTeamTurn = 0;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        System.out.println("getTeamTurn");
        if(whiteTeamTurn > blackTeamTurn) {
            return TeamColor.BLACK;
        } else {
            return TeamColor.WHITE;
        }

    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        System.out.println("setTeamTurn");
        if(team == TeamColor.WHITE) {
            whiteTeamTurn++;
        } else {
            blackTeamTurn++;
        }
        System.out.println("White team turn: " + whiteTeamTurn + " Black team turn: " + blackTeamTurn);
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        System.out.println("validMoves");
        if(getBoard().getPiece(startPosition) == null) {
            return null;
        }
        System.out.println("Piece not null");
        ChessPiece piece = getBoard().getPiece(startPosition);
        Collection<ChessMove> moves =  piece.pieceMoves(getBoard(), startPosition);
        System.out.println("Got past pieceMoves");
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        System.out.println("makeMove");
        System.out.println("Piece at start: " + getBoard().getPiece(move.getStartPosition()) + " Start Position: " +move.getStartPosition() +
                " End position: " + move.getEndPosition());
        Collection<ChessMove> validMovesList = validMoves(move.getStartPosition());
        System.out.println("Got past validMoves");
        if(validMovesList == null) {
            throw new InvalidMoveException();
        }
        int checkMove = 0;
       // System.out.println("Size: " + validMovesList.size());
        for(ChessMove check : validMovesList) {
            //System.out.println(check);
           // System.out.println(move.getEndPosition());
            //System.out.println(check.getEndPosition());
            if(check.getEndPosition().equals(move.getEndPosition())) {
                checkMove = 1;
                break;
            }
        }
        System.out.println("Check Move");
       if(checkMove == 0) {
           throw new InvalidMoveException();
        } else {
            ChessBoard newBoard = gameBoard;
            newBoard.addPiece(move.getEndPosition(), getBoard().getPiece(move.getStartPosition()));
            newBoard.addPiece(move.getStartPosition(), null);
            setBoard(newBoard);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        System.out.println("setBoard");
        /*if(whiteTeamTurn == 0 && blackTeamTurn == 0) {
            gameBoard.resetBoard();
        } else {
            gameBoard = board;
        }*/
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
       System.out.println("getBoard");
       //setBoard(gameBoard);
       return gameBoard;
    }
}


