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
   // private int teamTurn = 1;
    private int whiteTeamTurn = 0;
    private int blackTeamTurn = 0;
    private TeamColor turn;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        //System.out.println("getTeamTurn");
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        //System.out.println("setTeamTurn");
        if(team == TeamColor.WHITE) {
            turn = TeamColor.WHITE;
        } else {
            turn = TeamColor.BLACK;
        }
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
       if(getTeamTurn() != getBoard().getPiece(startPosition).getTeamColor()) {
            return null;
       }
        //System.out.println("validMoves");
        if(getBoard().getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece piece = getBoard().getPiece(startPosition);
        Collection<ChessMove> moves =  piece.pieceMoves(getBoard(), startPosition);
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(isInCheck(getBoard().getPiece(move.getStartPosition()).getTeamColor()) &&
                getBoard().getPiece(move.getStartPosition()).getPieceType() != ChessPiece.PieceType.KING) {
            throw new InvalidMoveException("King is in check and you're not moving King");
        }
        //System.out.println("makeMove");
        System.out.println("Piece at start: " + getBoard().getPiece(move.getStartPosition()) + " Start Position: " +move.getStartPosition() +
                " End position: " + move.getEndPosition());
        Collection<ChessMove> validMovesList = validMoves(move.getStartPosition());
        if(validMovesList == null) {
            throw new InvalidMoveException("either piece is null or it is not the teams turn");
        }
        int checkMove = 0;
       // System.out.println("Size: " + validMovesList.size());
        for(ChessMove check : validMovesList) {
            if(check.getEndPosition().equals(move.getEndPosition())) {
                checkMove = 1;
                break;
            }
        }
       if(checkMove == 0) {
           //System.out.println("invalid");
           throw new InvalidMoveException("move is not a valid move for piece");
        } else {
            ChessBoard newBoard = gameBoard;
            if(getBoard().getPiece(move.getEndPosition()) != null && getBoard().getPiece(move.getEndPosition()).getTeamColor() !=
                     getBoard().getPiece(move.getStartPosition()).getTeamColor()) {
                newBoard.addPiece(move.getEndPosition(), null);
            }
            newBoard.addPiece(move.getEndPosition(), getBoard().getPiece(move.getStartPosition()));
            if(getBoard().getPiece(move.getStartPosition()).getTeamColor() == TeamColor.WHITE) {
                setTeamTurn(TeamColor.BLACK);
            } else {
                setTeamTurn(TeamColor.WHITE);
            }
            newBoard.addPiece(move.getStartPosition(), null);
            setBoard(newBoard);
            //System.out.println(newBoard);
           System.out.println("True");
        }
    }

    public boolean checkOpposingTeam(TeamColor team, ChessPosition kingPosition) {
        TeamColor opposingTeam;
        ChessBoard board = getBoard();
        if(team == TeamColor.WHITE) {
            opposingTeam = TeamColor.BLACK;
        } else {
            opposingTeam = TeamColor.WHITE;
        }
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i, j);
                if(board.getPiece(boardPosition) != null && board.getPiece(boardPosition).getTeamColor().equals(opposingTeam)) {
                   // System.out.println("Piece type: " + getBoard().getPiece(boardPosition).getPieceType() + " Piece Color: " + getBoard().getPiece(boardPosition).getTeamColor());
                   Collection<ChessMove> moves =  board.getPiece(boardPosition).pieceMoves(getBoard(), boardPosition);
                   for(ChessMove check : moves) {
                      // System.out.println(check);
                       if(check.getEndPosition().equals(kingPosition)) {
                           return true;
                       }
                   }
                }
            }
        }
       // System.out.println("King Position: " + kingPosition);
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard findKing = getBoard();
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPosition kingPosition = null;
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i, j);
                if(findKing.getPiece(boardPosition) != null && findKing.getPiece(boardPosition).equals(king)) {
                    kingPosition = boardPosition;
                }
            }
        }
        return checkOpposingTeam(teamColor, kingPosition);
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
       // System.out.println("setBoard");
       // whiteTeamTurn = 0;
        //blackTeamTurn = 0;
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
      // System.out.println("getBoard");
       //setBoard(gameBoard);
       return gameBoard;
    }
}


