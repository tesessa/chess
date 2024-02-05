package chess;

import java.util.*;

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
  //  ChessPosition kingPosition = null;

   private ArrayList<ChessMove> killKing = new ArrayList<ChessMove>();

    private boolean inCheck = false;


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
        Collection<ChessMove> moves;
        TeamColor team = getBoard().getPiece(startPosition).getTeamColor();
       if(getTeamTurn() != team) {
            return null;
       }
        if(getBoard().getPiece(startPosition) == null) {
            return null;
        }
        if(isInCheck(team)) {
            ChessPiece kingPiece = new ChessPiece(team, ChessPiece.PieceType.KING);
            ChessPosition kingPosition = findKingPosition(team);
            moves = getOutOfCheck(team, kingPosition);
            Collection<ChessMove> temp;
            temp = kingPiece.pieceMoves(getBoard(), kingPosition);
            for(ChessMove check : temp) {
                if(!kingIsUnsafe(team, check.getEndPosition())) {
                    moves.add(check);
                }
            }
        } else {
            ChessPiece piece = getBoard().getPiece(startPosition);
            moves = piece.pieceMoves(getBoard(), startPosition);
        }
        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //if(isInCheck(getBoard().getPiece(move.getStartPosition()).getTeamColor())) {
        //    validMoves(move.getStartPosition());
        //}
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
           throw new InvalidMoveException("move is not a valid move for piece");
        } else {
            ChessBoard newBoard = gameBoard;
            ChessPiece pieceEndPosition = getBoard().getPiece(move.getEndPosition());
            ChessPiece originalPiece = getBoard().getPiece(move.getStartPosition());
            if(pieceEndPosition != null && pieceEndPosition.getTeamColor() !=  originalPiece.getTeamColor()) {
                newBoard.addPiece(move.getEndPosition(), null);
            }
            if(originalPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
                if (originalPiece.getTeamColor() == TeamColor.WHITE && move.getEndPosition().getRow() == 8) {
                    ChessPiece piece = new ChessPiece(TeamColor.WHITE, move.getPromotionPiece());
                    newBoard.addPiece(move.getEndPosition(), piece);
                } else if (originalPiece.getTeamColor() == TeamColor.BLACK && move.getEndPosition().getRow() == 1) {
                    ChessPiece piece = new ChessPiece(TeamColor.BLACK, move.getPromotionPiece());
                    newBoard.addPiece(move.getEndPosition(), piece);
                }
            } else {
                newBoard.addPiece(move.getEndPosition(), originalPiece);
            }
            if(originalPiece.getTeamColor() == TeamColor.WHITE) {
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

    /**
     * finds pieces that can capture enemy piece that causes piece to be in check
     *
     * @param team
     * @param kingPosition
     * @return
     */
    public Collection<ChessMove> getOutOfCheck(TeamColor team, ChessPosition kingPosition) {
        HashSet<ChessMove> killEnemy = new HashSet<ChessMove>();
        Collection<ChessMove> enemyPositions = enemyPositions(team, kingPosition);
        ChessBoard board  = getBoard();
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i, j);
                ChessPiece pieceInLoop = board.getPiece(boardPosition);
                if(pieceInLoop != null && pieceInLoop.getTeamColor().equals(team)) {
                    Collection<ChessMove> moves = pieceInLoop.pieceMoves(board, boardPosition);
                    for(ChessMove check : moves) {
                        for(ChessMove enemy : enemyPositions) {
                            if(check.getEndPosition().equals(enemy.getStartPosition())) {
                                killEnemy.add(check);
                            }
                        }
                    }
                }
            }
        }
        return killEnemy;
    }
    public Collection<ChessMove> enemyPositions(TeamColor team, ChessPosition kingPosition) {
        HashSet<ChessMove> enemyPositions = new HashSet<ChessMove>();
        ChessBoard board = getBoard();
        //loop through board
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i,j);
                ChessPiece pieceInLoop = board.getPiece(boardPosition);
                if(pieceInLoop != null && !pieceInLoop.getTeamColor().equals(team)) {
                    Collection<ChessMove> moves = pieceInLoop.pieceMoves(board, boardPosition);
                    for(ChessMove move : moves) {
                        if(move.getEndPosition().equals(kingPosition)) {
                            enemyPositions.add(move);
                           // inCheck = true;
                        }
                    }
                }
            }
        }
        return enemyPositions;
    }

    public ChessPosition findKingPosition(TeamColor team) {
        ChessBoard findKing = getBoard();
        ChessPiece king = new ChessPiece(team, ChessPiece.PieceType.KING);
        ChessPosition kingPosition = null;
        // ChessPosition kingPosition = null;
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i, j);
                if(findKing.getPiece(boardPosition) != null && findKing.getPiece(boardPosition).equals(king)) {
                    kingPosition = boardPosition;
                }
            }
        }
        return kingPosition;
    }

    /**
     * Checks to see if the opponent team can capture the team
     * loops through the opponents and sees if their end positions
     * matches the opposing teams kings position
     * @param team
     * @param kingPosition
     * @return
     */
    public boolean kingIsUnsafe(TeamColor team, ChessPosition kingPosition) {
        int inCheck = 0;
        TeamColor opposingTeam;
        ChessBoard board = getBoard();
        if(team == TeamColor.WHITE) {
            opposingTeam = TeamColor.BLACK;
        } else {
            opposingTeam = TeamColor.WHITE;
        }
       // Collection<ChessMove> killKing = null;
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i, j);
                ChessPiece pieceInLoop = board.getPiece(boardPosition);
                if(pieceInLoop != null && pieceInLoop.getTeamColor().equals(opposingTeam)) {
                   // System.out.println("Piece type: " + getBoard().getPiece(boardPosition).getPieceType() + " Piece Color: " + getBoard().getPiece(boardPosition).getTeamColor());
                   Collection<ChessMove> moves =  pieceInLoop.pieceMoves(board, boardPosition);
                   System.out.println("Piece type: " + pieceInLoop.getPieceType() + " Piece Color: " + pieceInLoop.getTeamColor() + "\n" + moves);
                   for(ChessMove check : moves) {
                       if (pieceInLoop.getPieceType() == ChessPiece.PieceType.PAWN) {
                           int row = check.getStartPosition().getRow();
                           int col = check.getStartPosition().getColumn();
                           if (pieceInLoop.getTeamColor() == TeamColor.BLACK) {
                               ChessPosition opponent1 = new ChessPosition(row - 1, col - 1);
                               ChessPosition opponent2 = new ChessPosition(row - 1, col + 1);
                               if(opponent1.equals(kingPosition) || opponent2.equals(kingPosition)) {
                                   inCheck = 1;
                               }
                           } else if(pieceInLoop.getTeamColor() == TeamColor.WHITE) {
                               ChessPosition opponent1 = new ChessPosition(row+1, col-1);
                               ChessPosition opponent2 = new ChessPosition(row+1, col+1);
                               if(opponent1.equals(kingPosition) || opponent2.equals(kingPosition)) {
                                   inCheck = 1;
                               }
                           }
                       }
                       if(check.getEndPosition().equals(kingPosition)) {
                          // ChessMove opponent = new ChessMove(check.getStartPosition(), check.getEndPosition());
                           //System.out.println("opponent: " + opponent);
                          // System.out.println("first: " + killKing);
                           //killKing.add(opponent);
                           //System.out.println("Second: " + killKing);
                           //validMoves(check.getStartPosition());
                           inCheck = 1;
                       }
                   }
                }
            }
        }
       // for(ChessMove moves : killKing) {
       //     System.out.println(getBoard().getPiece(moves.getStartPosition()).getPieceType() + " " + getBoard().getPiece(moves.getStartPosition()).getTeamColor() + " " + moves);
        //}
       // System.out.println("checkOpposingTeam " + inCheck);
        if(inCheck == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        isInCheckmate(teamColor);
        return kingIsUnsafe(teamColor, kingPosition);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ChessPiece kingPiece = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        ChessPosition kingPosition = findKingPosition(teamColor);
        Collection <ChessMove> moves = getOutOfCheck(teamColor, kingPosition);
        Collection<ChessMove> temp;
        temp = kingPiece.pieceMoves(getBoard(), kingPosition);
        for(ChessMove check : temp) {
            if(!kingIsUnsafe(teamColor, check.getEndPosition())) {
                moves.add(check);
            }
        }
        if(moves.isEmpty()) {
            return true;
        } else {
            return false;
        }
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
        killKing = new ArrayList<ChessMove>();
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


