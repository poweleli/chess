package chess;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece currPiece = board.getPiece(myPosition);

        return switch (currPiece.getPieceType()) {
            case KING -> kingMoves(board, myPosition, currPiece);
            case QUEEN -> queenMoves(board, myPosition, currPiece);
            case ROOK -> rookMoves(board, myPosition, currPiece);
            case BISHOP -> bishopMoves(board, myPosition, currPiece);
            case KNIGHT -> knightMoves(board, myPosition, currPiece);
            case PAWN -> pawnMoves(board, myPosition, currPiece);
        };
    }

    public Boolean[] checkValidPos(ChessPosition posPos,  ChessBoard board, ChessPiece currPiece) {
        // check edge of board
        if (posPos.getColumn() >= 1 &&
            posPos.getColumn() <= 8 &&
            posPos.getRow() >= 1 &&
            posPos.getRow() <= 8) {
            // check actual piece
            ChessPiece posPiece = board.getPiece(posPos);

            if (board.getPiece(posPos) == null) {
                // ending position is null
                return new Boolean[]{Boolean.TRUE, Boolean.FALSE};
            } else if (!currPiece.getTeamColor().equals(posPiece.getTeamColor())) {
                //ending position takes a piece
                return new Boolean[]{Boolean.TRUE, Boolean.TRUE};
            } else {
                // ending position is a same team piece
                return new Boolean[]{Boolean.FALSE, Boolean.FALSE};
            }
        // off of the board
        } else {return new Boolean[] {Boolean.FALSE, Boolean.FALSE};}
    }

    public Collection<ChessMove> straightMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece, int[][] direction) {
        HashSet<ChessMove> posChessMoves = new HashSet<ChessMove>();
        for (int[] pair : direction) {
            // iterate through each direction
            for (int k = 1; k < 8; k++) {
                // check if valid
                ChessPosition posPos = new ChessPosition(myPosition.getRow() + (k * pair[0]), myPosition.getColumn() + (k * pair[1]));
                Boolean[] validPos = checkValidPos(posPos, board, currPiece);
                if (validPos[0]) {
                    posChessMoves.add(new ChessMove(myPosition, posPos, null));
                    // if taking a piece stop progress
                    if (validPos[1]) {break;}
                } else {break;}
            }
        }
        return posChessMoves;
    }

    public Collection<ChessMove> oneMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece, int[][] moves) {
        HashSet<ChessMove> posChessMoves = new HashSet<ChessMove>();
        for (int[] pair : moves) {
            // check if valid
            ChessPosition posPos = new ChessPosition(myPosition.getRow() + pair[0], myPosition.getColumn() + pair[1]);
            Boolean[] validPos = checkValidPos(posPos, board, currPiece);
            if (validPos[0]) {
                posChessMoves.add(new ChessMove(myPosition, posPos, null));
            }
        }
        return posChessMoves;
    }

    public Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece){
        int[][] moves = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1},
                                    {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        return oneMoves(board, myPosition, currPiece, moves);
    }

    public Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece){
        int[][] direction = new int[][]{{-1, -1}, {-1, 0}, {-1, 1}, {0, -1},
                                        {0, 1}, {1, -1}, {1, 0}, {1, 1}};
        return straightMoves(board, myPosition, currPiece, direction);
    }

    public Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece){
        int[][] direction = new int[][]{{0, 1}, {0, -1}, {1, 0},{-1, 0}};
        return straightMoves(board, myPosition, currPiece, direction);
    }

    public Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece){
        int[][] direction = new int[][]{{1, 1}, {1, -1}, {-1, 1},{-1, -1}};
        return straightMoves(board, myPosition, currPiece, direction);
    }


    public Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece){
        int[][] moves = new int[][]{{1, 2}, {2, 1}, {2, -1}, {1, -2},
                                        {-2, 1}, {-2, -1}, {-1,2}, {-1,-2}};
        return oneMoves(board, myPosition, currPiece, moves);
    }

    public Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece){
        HashSet<ChessMove> posChessMoves = new HashSet<ChessMove>();
        //check the color of the current piece
        ChessGame.TeamColor currColor = currPiece.getTeamColor();
        int i = currColor.equals(ChessGame.TeamColor.WHITE) ? 1 : -1;
        int startRow = currColor.equals(ChessGame.TeamColor.WHITE) ? 2 : 7;
        int endRow = currColor.equals(ChessGame.TeamColor.WHITE) ? 8 : 1;

        int[][] moves = new int[][]{{i, 0}, {i, 1}, {i, -1}};

        for (int[] pair : moves) {
            ChessPosition posPos = new ChessPosition(myPosition.getRow() + pair[0], myPosition.getColumn() + pair[1]);
            if (posPos.getColumn() >= 1 &&
                    posPos.getColumn() <= 8 &&
                    posPos.getRow() >= 1 &&
                    posPos.getRow() <= 8) {
                // get piece at possible position
                ChessPiece posPiece = board.getPiece(posPos);
                // check moving straight
                if (pair[1] == 0) {
                    if (board.getPiece(posPos) == null) {
                        // check if on start row
                        if (myPosition.getRow() == startRow) {
                            posChessMoves.add(new ChessMove(myPosition, posPos, null));
                            posPos = new ChessPosition(myPosition.getRow() + 2 * pair[0], myPosition.getColumn() + pair[1]);
                            if (board.getPiece(posPos) == null) {
                                posChessMoves.add(new ChessMove(myPosition, posPos, null));
                            }
                        }
                        // check if moving to the end row
                        else if (posPos.getRow() == endRow) {
                            posChessMoves.add(new ChessMove(myPosition, posPos, PieceType.QUEEN));
                            posChessMoves.add(new ChessMove(myPosition, posPos, PieceType.ROOK));
                            posChessMoves.add(new ChessMove(myPosition, posPos, PieceType.KNIGHT));
                            posChessMoves.add(new ChessMove(myPosition, posPos, PieceType.BISHOP));
                        }
                        // otherwise just add the pawn space
                        else {
                            posChessMoves.add(new ChessMove(myPosition, posPos, null));
                        }
                    }
                }
                // add diagonal if you can take the other team
                else {
                    if (board.getPiece(posPos) != null && !board.getPiece(posPos).getTeamColor().equals(currColor)) {
                        if (posPos.getRow() == endRow) {
                            posChessMoves.add(new ChessMove(myPosition, posPos, PieceType.QUEEN));
                            posChessMoves.add(new ChessMove(myPosition, posPos, PieceType.ROOK));
                            posChessMoves.add(new ChessMove(myPosition, posPos, PieceType.KNIGHT));
                            posChessMoves.add(new ChessMove(myPosition, posPos, PieceType.BISHOP));
                        } else {
                            posChessMoves.add(new ChessMove(myPosition, posPos, null));
                        }
                    }
                }
            }
        }
        return posChessMoves;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return this.type + " " + this.pieceColor;
    }
}
