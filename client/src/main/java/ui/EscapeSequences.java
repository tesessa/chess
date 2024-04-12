package ui;

import chess.*;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class contains constants and functions relating to ANSI Escape Sequences that are useful in the Client display
 */
public class EscapeSequences {

    private static final String UNICODE_ESCAPE = "\u001b";
    private static final String ANSI_ESCAPE = "\033";

    public static final String ERASE_SCREEN = UNICODE_ESCAPE + "[H" + UNICODE_ESCAPE + "[2J";
    public static final String ERASE_LINE = UNICODE_ESCAPE + "[2K";

    public static final String SET_TEXT_BOLD = UNICODE_ESCAPE + "[1m";
    public static final String SET_TEXT_FAINT = UNICODE_ESCAPE + "[2m";
    public static final String RESET_TEXT_BOLD_FAINT = UNICODE_ESCAPE + "[22m";
    public static final String SET_TEXT_ITALIC = UNICODE_ESCAPE + "[3m";
    public static final String RESET_TEXT_ITALIC = UNICODE_ESCAPE + "[23m";
    public static final String SET_TEXT_UNDERLINE = UNICODE_ESCAPE + "[4m";
    public static final String RESET_TEXT_UNDERLINE = UNICODE_ESCAPE + "[24m";
    public static final String SET_TEXT_BLINKING = UNICODE_ESCAPE + "[5m";
    public static final String RESET_TEXT_BLINKING = UNICODE_ESCAPE + "[25m";

    private static final String SET_TEXT_COLOR = UNICODE_ESCAPE + "[38;5;";
    private static final String SET_BG_COLOR = UNICODE_ESCAPE + "[48;5;";

    public static final String SET_TEXT_COLOR_BLACK = SET_TEXT_COLOR + "0m";
    public static final String SET_TEXT_COLOR_LIGHT_GREY = SET_TEXT_COLOR + "242m";
    public static final String SET_TEXT_COLOR_DARK_GREY = SET_TEXT_COLOR + "235m";
    public static final String SET_TEXT_COLOR_RED = SET_TEXT_COLOR + "160m";
    public static final String SET_TEXT_COLOR_GREEN = SET_TEXT_COLOR + "46m";
    public static final String SET_TEXT_COLOR_YELLOW = SET_TEXT_COLOR + "226m";
    public static final String SET_TEXT_COLOR_BLUE = SET_TEXT_COLOR + "12m";
    public static final String SET_TEXT_COLOR_MAGENTA = SET_TEXT_COLOR + "5m";
    public static final String SET_TEXT_COLOR_WHITE = SET_TEXT_COLOR + "15m";
    public static final String RESET_TEXT_COLOR = SET_TEXT_COLOR + "0m";

    public static final String SET_BG_COLOR_BLACK = SET_BG_COLOR + "0m";
    public static final String SET_BG_COLOR_LIGHT_GREY = SET_BG_COLOR + "242m";
    public static final String SET_BG_COLOR_DARK_GREY = SET_BG_COLOR + "235m";
    public static final String SET_BG_COLOR_RED = SET_BG_COLOR + "160m";
    public static final String SET_BG_COLOR_GREEN = SET_BG_COLOR + "46m";
    public static final String SET_BG_COLOR_DARK_GREEN = SET_BG_COLOR + "22m";
    public static final String SET_BG_COLOR_YELLOW = SET_BG_COLOR + "226m";
    public static final String SET_BG_COLOR_BLUE = SET_BG_COLOR + "12m";
    public static final String SET_BG_COLOR_MAGENTA = SET_BG_COLOR + "5m";
    public static final String SET_BG_COLOR_WHITE = SET_BG_COLOR + "15m";
    public static final String RESET_BG_COLOR = SET_BG_COLOR + "0m";

    public static final String WHITE_KING = " ♔ ";
    public static final String WHITE_QUEEN = " ♕ ";
    public static final String WHITE_BISHOP = " ♗ ";
    public static final String WHITE_KNIGHT = " ♘ ";
    public static final String WHITE_ROOK = " ♖ ";
    public static final String WHITE_PAWN = " ♙ ";
    public static final String BLACK_KING = " ♚ ";
    public static final String BLACK_QUEEN = " ♛ ";
    public static final String BLACK_BISHOP = " ♝ ";
    public static final String BLACK_KNIGHT = " ♞ ";
    public static final String BLACK_ROOK = " ♜ ";
    public static final String BLACK_PAWN = " ♟ ";
    public static final String EMPTY = " \u2003 ";

    private static final int BOARD_SIZE_IN_SQUARES = 8;

    private static final int EMPTY_BOARD_SIZE = 4;
    private static final int SQUARE_SIZE_IN_CHARS = 3;
    private static final int LINE_WIDTH_IN_CHARS = 1;

    public static void main(String[] args) throws InvalidMoveException {
        var out = new PrintStream(System.out, true);
        ChessGame game = new ChessGame();
        ChessBoard board = game.getBoard();
        ChessPosition position = new ChessPosition(1,2);
        printLegalMoves(game, position, ChessGame.TeamColor.WHITE);
        int [][] moves = new int[8][8];
        board.resetBoard();
        String[][] arr1 = convertBoard(board);
        out.print(ERASE_SCREEN);
        out.print(SET_TEXT_BOLD);
        drawBoardWhite(out, board, arr1, moves);
        out.print(SET_BG_COLOR_BLACK);
        out.println(EMPTY.repeat(12));
        drawBoardBlack(out, board, arr1, moves);
    }

    public  static void printWhiteBoard(ChessBoard board, int[][] moves) {
        var out = new PrintStream(System.out, true);
        String[][] arr1 = convertBoard(board);
        out.print(ERASE_SCREEN);
        out.print(SET_TEXT_BOLD);
        drawBoardWhite(out, board, arr1, moves);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(ERASE_SCREEN);
    }

    public static void printBlackBoard(ChessBoard board, int[][] moves) {
        var out = new PrintStream(System.out, true);
        String[][] arr1 = convertBoard(board);
        out.print(ERASE_SCREEN);
        out.print(SET_TEXT_BOLD);
        drawBoardBlack(out, board, arr1, moves);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(ERASE_SCREEN);
    }


    public static void printLegalMoves(ChessGame game, ChessPosition position, ChessGame.TeamColor team) throws InvalidMoveException {
        HashSet<ChessMove> moves = (HashSet<ChessMove>) game.validMoves(position);
        int arr [][] = convertLegalMoves(moves, team);
        if(team == ChessGame.TeamColor.WHITE) {
            printWhiteBoard(game.getBoard(), arr);
        } else if (team == ChessGame.TeamColor.BLACK) {
            printBlackBoard(game.getBoard(), arr);
        } else {
            printWhiteBoard(game.getBoard(), arr);
        }
    }

    public static int[][] convertLegalMoves(HashSet<ChessMove> moves, ChessGame.TeamColor team) {
        int arr [][]= new int[8][8];
        for(var loop : moves) {
            ChessPosition start = loop.getStartPosition();
            ChessPosition position = loop.getEndPosition();
            System.out.println("Start " + start);
            System.out.println("End " + position);
            //System.out.println("Valid: " + position);
            //System.out.println("Start: " + start);
           // if(team == ChessGame.TeamColor.BLACK) {
                arr[8-position.getRow()][position.getColumn()-1] = 1;
                arr[8-start.getRow()][start.getColumn()-1] = 2;
            //}
        }
        return arr;
    }
    public static String moveCursorToLocation(int x, int y) { return UNICODE_ESCAPE + "[" + y + ";" + x + "H"; }

    public static String[][]  convertBoard(ChessBoard board) {
        String arr [][] = new String[8][8];
        for(int i = 1; i < 9; i++) {
            for(int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                if(piece != null) {
                    ChessPiece.PieceType type = piece.getPieceType();
                    if(piece.getPieceType() == ChessPiece.PieceType.BISHOP) {
                        arr[i-1][j-1] = " B ";
                    } else if (piece.getPieceType() == ChessPiece.PieceType.KING) {
                        arr[i-1][j-1] = " K ";
                    } else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                        arr[i-1][j-1] = " N ";
                    } else if (piece.getPieceType() == ChessPiece.PieceType.PAWN) {
                        arr[i-1][j-1] = " P ";
                    } else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN) {
                        arr[i-1][j-1] = " Q ";
                    } else if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
                        arr[i-1][j-1] = " R ";
                    }
                } else {
                    arr[i-1][j-1] = "   ";
                }
            }
        }
        return arr;
    }

    public static void drawBoardWhite(PrintStream out, ChessBoard board, String[][] arr, int[][] moves) {
        String horizontalBoard1[] = {" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
       // String horizontalBoard1[] = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        String verticalBoard1[] = {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
       // String verticalBoard1[] = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        out.print(SET_TEXT_COLOR_BLACK);
        drawBorders(out, horizontalBoard1);
        drawRows(out, verticalBoard1, board, arr, true, moves);
        out.print(SET_TEXT_COLOR_BLACK);
        drawBorders(out, horizontalBoard1);
    }

    public static void drawBoardBlack(PrintStream out, ChessBoard board, String[][] arr, int[][]moves) {
       // String horizontalBoard2[] = {" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        String horizontalBoard2[] = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
       // String verticalBoard2[] = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        String verticalBoard2[] = {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
        out.print(SET_TEXT_COLOR_BLACK);
        drawBorders(out, horizontalBoard2);
        drawRows(out, verticalBoard2, board, arr, false, moves);
        out.print(SET_TEXT_COLOR_BLACK);
        drawBorders(out, horizontalBoard2);
    }

    public static void drawBorders(PrintStream out, String horizontalChar[]) {
        for(int vertical = 0; vertical < 3; vertical++) {
            for(int horizontal = 0; horizontal < BOARD_SIZE_IN_SQUARES+2; horizontal++) {
                if(horizontal != 0 && horizontal != 9 && vertical == 1) {
                    out.print(EMPTY.repeat(1));
                    out.print(horizontalChar[horizontal-1]);
                    out.print(EMPTY.repeat(1));
                } else {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(EMPTY.repeat(3));
                }
            }
            out.println();
        }
    }

    public static void drawRows(PrintStream out, String[] rows, ChessBoard board, String[][] arr, boolean white, int[][] moves) {
        if(white) {
            for(int row = BOARD_SIZE_IN_SQUARES-1; row >= 0; row--) {
                drawRowOfSquares(out,row, rows, board, arr, moves, false);
            }
        } else {
            for (int row = 0; row < BOARD_SIZE_IN_SQUARES; row++) {
                drawRowOfSquares(out, row, rows, board, arr, moves, true);
            }
        }
    }

    public static void drawRowOfSquares(PrintStream out, int row, String[] rows, ChessBoard board, String[][] arr, int[][] legalMoves, boolean black) {
        String verticalPos = String.valueOf(rows[row]);
        for(int squareRow = 0; squareRow < 3; squareRow++) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                String print;
                if(black) print = arr[row][boardCol];
                else print = arr[7-row][7-boardCol];
                if(legalMoves[row][boardCol] == 1) {
                    out.print(SET_BG_COLOR_GREEN);
                } else if(legalMoves[row][boardCol] == 2) {
                    out.print(SET_BG_COLOR_YELLOW);
                }
                if (boardCol == 0) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    if (squareRow == 1) {
                      //  out.print(SET_TEXT_COLOR_GREEN);
                        out.print(EMPTY.repeat(1));
                        out.print(verticalPos);
                        out.print(EMPTY.repeat(1));
                    } else out.print(EMPTY.repeat(3));
                }
                ChessPosition position = new ChessPosition(row+1, boardCol+1);
                ChessPiece piece = board.getPiece(position);
                if(piece != null) {
                    ChessGame.TeamColor color = piece.getTeamColor();
                    if(color == ChessGame.TeamColor.WHITE) out.print(SET_TEXT_COLOR_BLUE);
                    else out.print(SET_TEXT_COLOR_RED);
                }
                if (row % 2 == 0) {
                    if (boardCol % 2 == 0) {
                        backgroundBlack(out);
                        if(black) backgroundWhite(out);
                        if(legalMoves[row][boardCol] == 1) out.print(SET_BG_COLOR_GREEN);
                        else if(legalMoves[row][boardCol] == 2) out.print(SET_BG_COLOR_YELLOW);
                        if(squareRow == 1) {
                            out.print(EMPTY.repeat(1));
                            out.print(print);
                            out.print(EMPTY.repeat(1));
                        } else out.print(EMPTY.repeat(3));
                    } else {
                        backgroundWhite(out);
                        if(black) backgroundBlack(out);
                        if(legalMoves[row][boardCol] == 1) out.print(SET_BG_COLOR_GREEN);
                        else if(legalMoves[row][boardCol] == 2) out.print(SET_BG_COLOR_YELLOW);
                        if(squareRow == 1) {
                            out.print(EMPTY.repeat(1));
                            out.print(print);
                            out.print(EMPTY.repeat(1));
                        } else {
                            out.print(EMPTY.repeat(3));
                        }
                    }
                    ChessPosition p = new ChessPosition(row+1, boardCol+1);
                    ChessPiece pi = board.getPiece(p);
                    if(pi != null) {
                        ChessGame.TeamColor color = pi.getTeamColor();
                        if(color == ChessGame.TeamColor.WHITE) out.print(SET_TEXT_COLOR_BLUE);
                        else out.print(SET_TEXT_COLOR_RED);
                    }
                } else if(row%2 == 1 ){
                    if (boardCol % 2 == 0) {
                        backgroundWhite(out);
                        if(black) backgroundBlack(out);
                        if(legalMoves[row][boardCol] == 1) out.print(SET_BG_COLOR_GREEN);
                        else if(legalMoves[row][boardCol] == 2) out.print(SET_BG_COLOR_YELLOW);
                        if(squareRow == 1) {
                            out.print(EMPTY.repeat(1));
                            out.print(print);
                            out.print(EMPTY.repeat(1));
                        } else out.print(EMPTY.repeat(3));
                    } else {
                        backgroundBlack(out);
                        if(black) backgroundWhite(out);
                        if(legalMoves[row][boardCol] == 1) {
                            out.print(SET_BG_COLOR_GREEN);
                        } else if(legalMoves[row][boardCol] == 2) {
                            out.print(SET_BG_COLOR_YELLOW);
                        }
                        if(squareRow == 1) {
                            out.print(EMPTY.repeat(1));
                            out.print(print);
                            out.print(EMPTY.repeat(1));
                        } else {
                            out.print(EMPTY.repeat(3));
                        }
                    }
                }
                if (boardCol == 7) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    if (squareRow == 1) {
                        out.print(SET_TEXT_COLOR_BLACK);
                        out.print(EMPTY.repeat(1));
                        out.print(verticalPos);
                        out.print(EMPTY.repeat(1));
                    } else {
                        out.print(EMPTY.repeat(3));
                    }
                }
        }
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.println();
         }
    }


    private static void backgroundWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
    }

    private static void backgroundBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
    }

}
