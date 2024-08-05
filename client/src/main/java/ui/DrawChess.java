package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class DrawChess {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 0;

    // Chess pieces.
    private static final String[] WHITE_PIECES = {WHITE_ROOK, WHITE_KNIGHT, WHITE_BISHOP, WHITE_QUEEN, WHITE_KING, WHITE_BISHOP, WHITE_KNIGHT, WHITE_ROOK};
    private static final String[] BLACK_PIECES = {BLACK_ROOK, BLACK_KNIGHT, BLACK_BISHOP, BLACK_QUEEN, BLACK_KING, BLACK_BISHOP, BLACK_KNIGHT, BLACK_ROOK};
    private static final String WHITE_PAWN_STR = WHITE_PAWN;
    private static final String BLACK_PAWN_STR = BLACK_PAWN;
    private static final String EMPTY = "   ";

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawChessBoard(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawChessBoard(PrintStream out) {
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            drawRowOfSquares(out, boardRow);
            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                // Draw horizontal row separator.
                drawHorizontalLine(out);
                setBlack(out);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out, int boardRow) {
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                setSquareColor(out, boardRow, boardCol);

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPiece(out, getPiece(boardRow, boardCol));
                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // Draw vertical column separator.
                    setBlack(out);
                    out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
                }
            }
            out.println();
        }
    }

    private static void drawHorizontalLine(PrintStream out) {
        int boardSizeInSpaces = BOARD_SIZE_IN_SQUARES * SQUARE_SIZE_IN_PADDED_CHARS +
                (BOARD_SIZE_IN_SQUARES - 1) * LINE_WIDTH_IN_PADDED_CHARS;

        for (int lineRow = 0; lineRow < LINE_WIDTH_IN_PADDED_CHARS; ++lineRow) {
            setBlack(out);
            out.print(EMPTY.repeat(boardSizeInSpaces));
            out.println();
        }
    }

    private static void setSquareColor(PrintStream out, int row, int col) {
        if ((row + col) % 2 == 0) {
            out.print(SET_BG_COLOR_WHITE);
            out.print(SET_TEXT_COLOR_BLACK);
        } else {
            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_WHITE);
        }
    }

    private static void printPiece(PrintStream out, String piece) {
        out.print(piece);
    }

    private static String getPiece(int row, int col) {
        if (row == 0) return BLACK_PIECES[col];
        if (row == 1) return BLACK_PAWN_STR;
        if (row == 6) return WHITE_PAWN_STR;
        if (row == 7) return WHITE_PIECES[col];
        return EMPTY;
    }

    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    private static void printPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLACK);

        out.print(player);

        setWhite(out);
    }

    private static void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_WHITE);
    }

}

