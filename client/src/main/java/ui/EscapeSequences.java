package ui;

import java.io.PrintStream;

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

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true);
        out.print(ERASE_SCREEN);
       // out.println(SET_BG_COLOR_RED);
        out.print(SET_TEXT_BOLD);
        drawBoard2(out);
        out.print(SET_BG_COLOR_BLACK);
        out.println(EMPTY.repeat(12));
        drawBoard1(out);
        //drawRow1(out);
    }
    public static void printBoards() {
        var out = new PrintStream(System.out, true);
        drawBoard2(out);
        drawBoard1(out);
    }
    public static String moveCursorToLocation(int x, int y) { return UNICODE_ESCAPE + "[" + y + ";" + x + "H"; }

    public static void drawBoard1(PrintStream out) {
        String horizontalBoard1[] = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        String verticalBoard1[] = {" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
        String rowOutside[] = {" R "," N "," B "," K "," Q "," B "," N "," R "};
        String rowPawns[] = {" p "," p "," p "," p "," p "," p "," p "," p "};
        out.print(SET_TEXT_COLOR_BLACK);
        drawBorders(out, horizontalBoard1);
        drawRowEven(out, rowOutside, 8);
        drawRowOdd(out, rowPawns, 7);
        drawRows(out, verticalBoard1);
        drawRowEven(out, rowPawns, 2);
        drawRowOdd(out, rowOutside, 1);
        out.print(SET_TEXT_COLOR_BLACK);
        drawBorders(out, horizontalBoard1);
        //drawBorders(out, horizontalBoard1, verticalBoard1, rowOutside, rowPawns);
    }

    public static void drawBoard2(PrintStream out) {
        String horizontalBoard2[] = {" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        String verticalBoard2[] = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        String rowOutside[] = {" R "," N "," B "," K "," Q "," B "," N "," R "};
        String rowPawns[] = {" P "," P "," P "," P "," P "," P "," P "," P "};
        out.print(SET_TEXT_COLOR_BLACK);
        drawBorders(out, horizontalBoard2);
        drawRowEven(out, rowOutside, 1);
        drawRowOdd(out, rowPawns, 2);
        drawRows(out, verticalBoard2);
        drawRowEven(out, rowPawns, 7);
        drawRowOdd(out, rowOutside, 8);
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

    public static void drawRows(PrintStream out, String rows[]) {
        for(int row = 0; row < EMPTY_BOARD_SIZE; row++) {
            drawRowOfSquares(out, row, rows);
        }
    }
    public static void drawRowOfSquares(PrintStream out, int row, String rows[]) {
        String verticalPos = String.valueOf(rows[row+2]);
        for(int squareRow = 0; squareRow <3; squareRow++) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                if (boardCol == 0) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    if(squareRow == 1) {
                        out.print(SET_TEXT_COLOR_BLACK);
                        out.print(EMPTY.repeat(1));
                        out.print(verticalPos);
                        out.print(EMPTY.repeat(1));
                    } else {
                        out.print(EMPTY.repeat(3));
                    }
                }
                    if (row % 2 == 0) {
                        if (boardCol % 2 == 0) {
                            backgroundWhite(out);
                            out.print(EMPTY.repeat(3));
                        } else {
                            backgroundBlack(out);
                            out.print(EMPTY.repeat(3));
                        }
                    } else {
                        if (boardCol % 2 == 0) {
                            backgroundBlack(out);
                            out.print(EMPTY.repeat(3));
                        } else {
                            backgroundWhite(out);
                            out.print(EMPTY.repeat(3));
                        }
                  }
            }
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.println();
         }
    }

    public static void drawRowEven(PrintStream out, String[] characters, int verticalPos) {
        String row = " " + String.valueOf(verticalPos) + " ";
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        for(int squareRow = 0; squareRow <SQUARE_SIZE_IN_CHARS; squareRow++) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                if(boardCol == 0) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(SET_TEXT_COLOR_BLACK);
                    if(squareRow == 1) {
                        out.print(EMPTY.repeat(1));
                        out.print(row);
                        out.print(EMPTY.repeat(1));
                    } else {
                        out.print(EMPTY.repeat(3));
                    }
                }
                if(verticalPos == 2 || verticalPos == 1) {
                    out.print(SET_TEXT_COLOR_RED);
                } else if (verticalPos == 8 || verticalPos == 7) {
                    out.print(SET_TEXT_COLOR_BLUE);
                }
                    if (boardCol % 2 == 0) {
                        backgroundWhite(out);
                        if(squareRow == 1) {
                            out.print(EMPTY.repeat(1));
                            out.print(characters[boardCol]);
                            out.print(EMPTY.repeat(1));
                        } else {
                            out.print(EMPTY.repeat(3));
                        }
                    } else {
                        backgroundBlack(out);
                        if(squareRow == 1) {
                            out.print(EMPTY.repeat(1));
                            out.print(characters[boardCol]);
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

    public static void drawRowOdd(PrintStream out, String pawns[], int verticalPos) {
        String row = " " + String.valueOf(verticalPos) + " ";
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        for(int squareRow = 0; squareRow <SQUARE_SIZE_IN_CHARS; squareRow++) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; boardCol++) {
                if(boardCol == 0) {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                    out.print(SET_TEXT_COLOR_BLACK);
                    if(squareRow == 1) {
                        out.print(EMPTY.repeat(1));
                        out.print(row);
                        out.print(EMPTY.repeat(1));
                    } else {
                        out.print(EMPTY.repeat(3));
                    }
                }
                if(verticalPos == 1 || verticalPos == 2) {
                    out.print(SET_TEXT_COLOR_RED);
                } else if (verticalPos == 7 || verticalPos == 8) {
                    out.print(SET_TEXT_COLOR_BLUE);
                }
                if (boardCol % 2 == 0) {
                    backgroundBlack(out);
                    if(squareRow == 1) {
                        out.print(EMPTY.repeat(1));
                        out.print(pawns[boardCol]);
                        out.print(EMPTY.repeat(1));
                    } else {
                        out.print(EMPTY.repeat(3));
                    }
                } else {
                    backgroundWhite(out);
                    if(squareRow == 1) {
                        out.print(EMPTY.repeat(1));
                        out.print(pawns[boardCol]);
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

    private static void backgroundRed(PrintStream out) {
        out.print(SET_BG_COLOR_RED);
    }
}
