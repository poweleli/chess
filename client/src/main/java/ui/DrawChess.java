package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChess {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 0;

    // Padded characters.
    private static final String EMPTY = "   ";


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);
        ChessSetup chessSetup = new ChessSetup("black",
                new String[]{"1", "2", "3", "4", "5", "6", "7", "8"},
                new String[]{"h", "g", "f", "e", "d", "c", "b", "a" },
                new String[]{"R", "N", "B", "K", "Q", "B", "N", "R"},
                SET_TEXT_COLOR_RED,
                SET_TEXT_COLOR_BLUE);
        drawBoard(out, chessSetup);
        out.println();
        ChessSetup chessSetup2 = new ChessSetup("white",
                new String[]{"8", "7", "6", "5", "4", "3", "2", "1"},
                new String[]{ "a", "b", "c", "d", "e", "f", "g", "h" },
                new String[]{"R", "N", "B", "Q", "K", "B", "N", "R"},
                SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_RED);
        drawBoard(out, chessSetup2);


        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawBoard(PrintStream out, ChessSetup chessSetup){
        drawHeaderFooter(out, chessSetup);
        drawTicTacToeBoard(out, chessSetup);
        drawHeaderFooter(out, chessSetup);
    }

    private static void drawHeaderFooter(PrintStream out, ChessSetup chessSetup) {

        setBlack(out);

        out.print(" ".repeat(3));
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, chessSetup.colLabels()[boardCol]);

            out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
        }

        out.println();
    }

    private static void drawHeader(PrintStream out, String headerText) {
        out.print(" ".repeat(1));
        printHeaderText(out, headerText);
        out.print(" ".repeat(1));
    }

    private static void printHeaderText(PrintStream out, String player) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);

        out.print(player);

        setBlack(out);
    }

    private static void drawTicTacToeBoard(PrintStream out, ChessSetup chessSetup) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawRowHeaderFooter(out, boardRow, chessSetup);
            drawRowOfSquares(out, boardRow, chessSetup);

        }
    }

    private static void drawRowHeaderFooter(PrintStream out, int boardRow, ChessSetup chessSetup) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_GREEN);
        out.print(" " + chessSetup.rowLabels()[boardRow] + " ");
    }

    private static void getPlayer(PrintStream out, int boardRow, int boardCol, ChessSetup chessSetup) {
        if (boardRow == 0 || boardRow == 1) {
            out.print(chessSetup.myColor());
            if (boardRow == 0) {
                out.print(" " + chessSetup.pieceOrder()[boardCol]+ " ");
            } else {
                out.print(" " + "p"+ " ");
            }
        } else if (boardRow == 6 || boardRow == 7) {
            out.print(chessSetup.otherColor());
            if (boardRow == 7) {
                out.print(" " + chessSetup.pieceOrder()[boardCol]+ " ");
            } else {
                out.print(" " + "p" + " ");
            }

        } else {
            out.print(EMPTY);
        }
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow, ChessSetup chessSetup) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                if ((boardCol % 2 == 0 && boardRow % 2 == 1) ||
                        (boardCol % 2 == 1 && boardRow%2 == 0)) {
                    out.print(SET_BG_COLOR_DARK_GREY);
                } else {
                    out.print(SET_BG_COLOR_LIGHT_GREY);
                }
                getPlayer(out, boardRow, boardCol, chessSetup);

                if (boardCol == BOARD_SIZE_IN_SQUARES-1) {
                    drawRowHeaderFooter(out, boardRow, chessSetup);
                }
                setBlack(out);
            }
            out.println();
        }
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

}