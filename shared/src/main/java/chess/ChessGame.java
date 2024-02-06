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
    private TeamColor turn;

    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
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
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        ChessPiece piece = getBoard().getPiece(startPosition);
        TeamColor team = getBoard().getPiece(startPosition).getTeamColor();
        ChessBoard saveBoard = getBoard();
        ChessPosition kingPosition = findKingPosition(team);
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
            Collection<ChessMove> temp;
            temp = (HashSet<ChessMove>) piece.pieceMoves(getBoard(), startPosition);
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
            ChessPiece originalPiece = getBoard().getPiece(move.getStartPosition());
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
        }
    }

    /**
     * gets all move that a certain team can get
     *
     * @param team
     * @param kingPosition
     * @return
     */
    public Collection<ChessMove> getTeamMoves(TeamColor team, ChessPosition kingPosition) {
        HashSet<ChessMove> teamMoves = new HashSet<ChessMove>();
        ChessBoard board  = getBoard();
        for(int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition boardPosition = new ChessPosition(i, j);
                ChessPiece pieceInLoop = board.getPiece(boardPosition);
                if (pieceInLoop != null && pieceInLoop.getTeamColor().equals(team)) {
                    Collection<ChessMove> moves = pieceInLoop.pieceMoves(board, boardPosition);
                    for (ChessMove check : moves) {
                        teamMoves.add(check);
                   }
                }
            }
          }
        return teamMoves;
    }

    /**
     * finds the position of the king for a certain team
     * @param team
     * @return
     */
    public ChessPosition findKingPosition(TeamColor team) {
        ChessBoard findKing = getBoard();
        ChessPiece king = new ChessPiece(team, ChessPiece.PieceType.KING);
        ChessPosition kingPosition = null;
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
            return false;
        }
        ChessPiece kingPiece = getBoard().getPiece(kingPosition);
        HashSet<ChessMove> teamMoves = (HashSet<ChessMove>) getTeamMoves(teamColor, kingPosition); /* right here*/
        HashSet<ChessMove> moves = new HashSet<ChessMove>();
        Collection<ChessMove> temp;
        temp = kingPiece.pieceMoves(getBoard(), kingPosition);
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
        gameBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
       return gameBoard;
    }
}


