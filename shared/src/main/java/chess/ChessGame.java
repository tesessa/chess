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
    private int teamTurn = 0;
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
        //teamTurn++;
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
        //System.out.println("Is it calling");
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        ChessPiece piece = getBoard().getPiece(startPosition);
        TeamColor team = getBoard().getPiece(startPosition).getTeamColor();
        ChessBoard saveBoard = getBoard();
        if(getTeamTurn() == null) {
            setTeamTurn(team);
        }
       if(getTeamTurn() != team) {
            return null;
       }
        if(getBoard().getPiece(startPosition) == null) {
            return null;
        }
        if(isInCheck(team)) {
            //ChessBoard saveBoard = getBoard();
            //ChessPiece piece = getBoard().getPiece(startPosition);
            ChessPosition kingPosition = findKingPosition(team);
            Collection<ChessMove> enemyPositions = enemyPositions(team, kingPosition);
            Collection<ChessMove> temp;
            temp = (HashSet<ChessMove>) piece.pieceMoves(getBoard(), startPosition);
            //System.out.println(temp);
                    for(ChessMove iterate : temp) {
                        setBoard(new ChessBoard(saveBoard));
                        getBoard().addPiece(iterate.getEndPosition(), getBoard().getPiece(startPosition));
                        getBoard().addPiece(startPosition, null);
                        if (!kingIsUnsafe(team, kingPosition)) {
                            moves.add(iterate);
                        }
                    }
            setBoard(saveBoard);
        } else {
           // ChessPiece piece = getBoard().getPiece(startPosition);
            HashSet<ChessMove> test = new HashSet<ChessMove>();
            test = (HashSet<ChessMove>) piece.pieceMoves(getBoard(), startPosition);
            for(ChessMove iterate : test) {
                setBoard(new ChessBoard(saveBoard));
                getBoard().addPiece(iterate.getEndPosition(), piece);
                getBoard().addPiece(startPosition, null);
                if(!isInCheck(team)) {
                    moves.add(iterate);
                }
            }
            setBoard(saveBoard);
           // moves = (HashSet<ChessMove>) piece.pieceMoves(getBoard(), startPosition);
          //System.out.println("Piece type: " + getBoard().getPiece(startPosition).getPieceType() + " Color: " + getBoard().getPiece(startPosition).getTeamColor() + moves);
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
        //System.out.println("Piece at start: " + getBoard().getPiece(move.getStartPosition()) + " Start Position: " +move.getStartPosition() +
                //" End position: " + move.getEndPosition());
        Collection<ChessMove> validMovesList = validMoves(move.getStartPosition());
        if(validMovesList == null) {
            throw new InvalidMoveException("either piece is null or it is not the teams turn");
        }
        int checkMove = 0;
        for(ChessMove check : validMovesList) {
            if(check.getEndPosition().equals(move.getEndPosition())) {
                checkMove = 1;
                break;
            }
        }
       if(checkMove == 0) {
           throw new InvalidMoveException("move is not a valid move for piece");
        } else {
            ChessBoard newBoard = getBoard();
            ChessPiece pieceEndPosition = getBoard().getPiece(move.getEndPosition());
            ChessPiece originalPiece = getBoard().getPiece(move.getStartPosition());
           // if(pieceEndPosition != null) {
              //  newBoard.addPiece(move.getEndPosition(), null);
                //System.out.println("A" + move.getEndPosition());
           // }
            if (originalPiece.getPieceType() == ChessPiece.PieceType.PAWN && originalPiece.getTeamColor() == TeamColor.WHITE &&
                 move.getEndPosition().getRow() == 8) {
                 ChessPiece piece = new ChessPiece(TeamColor.WHITE, move.getPromotionPiece());
                 newBoard.addPiece(move.getEndPosition(), piece);
            } else if (originalPiece.getPieceType() == ChessPiece.PieceType.PAWN && originalPiece.getTeamColor() == TeamColor.BLACK &&
                 move.getEndPosition().getRow() == 1) {
                 ChessPiece piece = new ChessPiece(TeamColor.BLACK, move.getPromotionPiece());
                 newBoard.addPiece(move.getEndPosition(), piece);
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
           //System.out.println("True");
        }
    }

    /**
     * finds pieces that can capture enemy piece that causes piece to be in check
     *
     * @param team
     * @param kingPosition
     * @return
     */
    public Collection<ChessMove> getTeamMoves(TeamColor team, ChessPosition kingPosition) {
        HashSet<ChessMove> teamMoves = new HashSet<ChessMove>();
       // Collection<ChessMove> enemyPositions = enemyPositions(team, kingPosition);
        ChessBoard board  = getBoard();
        for(int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i, j);
                ChessPiece pieceInLoop = board.getPiece(boardPosition);
                if (pieceInLoop != null && pieceInLoop.getTeamColor().equals(team)) {
                    Collection<ChessMove> moves = pieceInLoop.pieceMoves(board, boardPosition);
                    for (ChessMove check : moves) {
                        teamMoves.add(check);
                      //  for (ChessMove enemy : enemyPositions) {
                            //if (check.getEndPosition().equals(enemy.getStartPosition())) {
                             //   killEnemy.add(check);
                           // }
                       // }
                   }
                }
            }
          }
       // }
        return teamMoves;
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
        ChessBoard board = getBoard();
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i, j);
                ChessPiece pieceInLoop = board.getPiece(boardPosition);
                if(pieceInLoop != null && !pieceInLoop.getTeamColor().equals(team)) {
                    if (pieceInLoop.getPieceType() == ChessPiece.PieceType.PAWN) {
                        int row = boardPosition.getRow();
                        int col = boardPosition.getColumn();
                        if (pieceInLoop.getTeamColor() == TeamColor.BLACK) {
                            ChessPosition opponent1 = new ChessPosition(row - 1, col - 1);
                            ChessPosition opponent2 = new ChessPosition(row - 1, col + 1);
                            if (opponent1.equals(kingPosition) || opponent2.equals(kingPosition)) {
                                inCheck = 1;
                            }
                        } else if (pieceInLoop.getTeamColor() == TeamColor.WHITE) {
                            ChessPosition opponent1 = new ChessPosition(row + 1, col - 1);
                            ChessPosition opponent2 = new ChessPosition(row + 1, col + 1);
                            if (opponent1.equals(kingPosition) || opponent2.equals(kingPosition)) {
                                inCheck = 1;
                            }
                        }
                    }
                   Collection<ChessMove> moves =  pieceInLoop.pieceMoves(board, boardPosition);
                 //  System.out.println("Piece type: " + pieceInLoop.getPieceType() + " Piece Color: " + pieceInLoop.getTeamColor() + "\n" + moves);
                   for(ChessMove check : moves) {
                       if(check.getEndPosition().equals(kingPosition)) {
                           inCheck = 1;
                       }
                   }
                }
            }
        }
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
        System.out.println("Team: " + teamColor);
       // System.out.println(getBoard());
       // boolean isCheckMate = true;
        // Get all possible moves for the king's team
        // for each of these moves
            // make a copy of the board
            // perform the move on the copy of the board
            // if(king is not in check on this board
             //  isCheckMate=false;
               //break;
        //return isCheckMate;
        ChessBoard tempBoard = new ChessBoard(getBoard());
        ChessPosition kingPosition = findKingPosition(teamColor);
        if(kingPosition == null) {
            System.out.println("A");
            return false;
        }
        ChessPiece kingPiece = getBoard().getPiece(kingPosition);
        HashSet<ChessMove> teamMoves = (HashSet<ChessMove>) getTeamMoves(teamColor, kingPosition); /* right here*/
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        Collection<ChessMove> temp;
        temp = kingPiece.pieceMoves(getBoard(), kingPosition);
        /*for (ChessMove check : temp) {
            if (tempBoard.getPiece(check.getEndPosition()) != null) {
                tempBoard.addPiece(check.getEndPosition(), null);
            }
            if (kingIsUnsafe(teamColor, check.getEndPosition())) {
                moves.remove(check);
            }
            //System.out.println("Is king unsafe: " + kingIsUnsafe(teamColor, check.getEndPosition()) + " End position: " + check.getEndPosition());
            if (!kingIsUnsafe(teamColor, check.getEndPosition())) { //this is checking if the king is unsafe at each of it's end points
                moves.add(check);
            }
        }*/
        for(ChessMove iterate : teamMoves) {
            tempBoard.addPiece(iterate.getEndPosition(), tempBoard.getPiece(iterate.getStartPosition()));
            if(!kingIsUnsafe(teamColor, kingPosition)) {
                moves.add(iterate);
            }
        }
        if(moves.isEmpty()) {
            if (temp.isEmpty()) {
                return false;
            } else {
                return true;
            }

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
        boolean staleMate = false;
        ChessBoard board = getBoard();
        Collection<ChessMove> moves;
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i,j);
                if(board.getPiece(boardPosition) != null && board.getPiece(boardPosition).getTeamColor().equals(teamColor)) {
                    ChessPiece piece = board.getPiece(boardPosition);
                    moves = piece.pieceMoves(board, boardPosition);
                    for(ChessMove check : moves) {
                        if(kingIsUnsafe(teamColor, check.getEndPosition())) {
                            staleMate = true;
                        } else {
                            staleMate = false;
                        }
                    }
                    if(moves.isEmpty()) {
                        staleMate = true;
                    }
                }
            }
        }
        return staleMate;
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
       //System.out.println("getBoard");
       //setBoard(gameBoard);
       return gameBoard;
    }
}


