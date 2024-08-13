package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

import static ui.EscapeSequences.*;

public class DrawChess {

    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 1;
    private static final int LINE_WIDTH_IN_PADDED_CHARS = 0;

    // Padded characters.
    private static final String EMPTY = "   ";

    private static final String[] rowLabels = new String[]{"8", "7", "6", "5", "4", "3", "2", "1"};
    private static final String[] colLabels = new String[]{ "a", "b", "c", "d", "e", "f", "g", "h" };
    private ChessBoard board;
    private Collection<ChessMove> highlightMoves;
    private ChessPosition highlightPos;

    public void drawBoard(ChessGame game, ChessPosition highlightPos) {
        if (highlightPos != null) {
            this.highlightPos = highlightPos;
            this.highlightMoves = game.validMoves(highlightPos);
        } else {
            this.highlightMoves = null;
        }
        this.board = game.getBoard();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        out.println();
        drawBoard(out);
    }

    private void drawBoard(PrintStream out) {
        drawHeaderFooter(out);
        for (int i=1; i<=8; i++) {
            drawRow(out, i);
        }
        drawHeaderFooter(out);
    }

    private void drawHeaderFooter(PrintStream out) {
        out.print(" ".repeat(3));
        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
            drawHeader(out, colLabels[boardCol]);
            out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
        }
        out.println();
    }

    private  void drawRow(PrintStream out, int i) {
//        draw
        out.print(" " + rowLabels[i-1] + " ");
        for (int j=1; j<=8; j++) {
            ChessPosition pos = new ChessPosition(i,j);
            getColor(out, pos);
            ChessPiece p = board.getPiece(pos);
            if (p != null) {
                printPiece(out, p);
            } else {
                out.print(EMPTY);
            }

        }
        resetColors(out);
        out.print(" " + rowLabels[i-1] + " ");
        out.print(EMPTY.repeat(LINE_WIDTH_IN_PADDED_CHARS));
        out.println();
    }

    private void printPiece(PrintStream out, ChessPiece p) {
        String piece = p.getTeamColor() + "_" + p.getPieceType();
        try {
            Field field = EscapeSequences.class.getField(p.getTeamColor() + "_" + p.getPieceType());
            out.print((String) field.get(null));
        } catch (Exception e) {
            out.print(EMPTY);
        }
    }

    private void getColor(PrintStream out, ChessPosition pos) {
        new ChessMove(highlightPos,pos, null);
        if (highlightMoves!= null && highlightMoves.contains(new ChessMove(highlightPos,pos, null))) {
            setHighlight(out);
        } else if ((pos.getRow() + pos.getColumn() + 1) % 2 == 0) {
            setWhite(out);
        } else {
            setBlack(out);
        }
    }

    private void drawHeader(PrintStream out, String player) {
        out.print(" ".repeat(1));
        out.print(player);
        out.print(" ".repeat(1));
    }

    private void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_TEXT_COLOR_YELLOW);
    }

    private void setWhite(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_YELLOW);
    }

    private void setHighlight(PrintStream out) {
        out.print(SET_BG_COLOR_GREEN);
        out.print(RESET_TEXT_COLOR);
    }

    private void resetColors(PrintStream out) {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }


}