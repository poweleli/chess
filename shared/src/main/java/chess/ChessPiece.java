package chess;

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
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

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

    public Boolean[] checkValidPos(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece, ChessPosition posPos) {
        if (posPos.getColumn() <= 8 &&
                posPos.getColumn() >= 1 &&
                posPos.getRow() <= 8 &&
                posPos.getRow() >= 1) {
            ChessPiece posPiece = board.getPiece(posPos);
            if (posPiece == null) {
                return new Boolean[]{Boolean.TRUE, Boolean.FALSE};
            } else if (!posPiece.getTeamColor().equals(currPiece.getTeamColor())) {
                return new Boolean[]{Boolean.TRUE, Boolean.TRUE};
            }
        }

        return new Boolean[]{Boolean.FALSE, Boolean.FALSE};
    }

    public Collection<ChessMove> staticMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece, int[][] direction) {
        HashSet<ChessMove> posMoves = new HashSet<ChessMove>();
        for (int[] pair: direction) {
            ChessPosition posPos = new ChessPosition(myPosition.getRow() + pair[0], myPosition.getColumn() + pair[1]);
            Boolean[] validPos = checkValidPos(board, myPosition, currPiece, posPos);
            if (validPos[0]) {
                posMoves.add(new ChessMove(myPosition, posPos, null));
            }
        }
        return posMoves;
    }

    public Collection<ChessMove> dynamicMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece, int[][] direction) {
        HashSet<ChessMove> posMoves = new HashSet<ChessMove>();
        for (int[] pair: direction) {
            for (int k=1; k<=8; k++) {
                ChessPosition posPos = new ChessPosition(myPosition.getRow() + k*pair[0], myPosition.getColumn() + k*pair[1]);
                Boolean[] validPos = checkValidPos(board, myPosition, currPiece, posPos);
                if (validPos[0]) {
                    posMoves.add(new ChessMove(myPosition, posPos, null));
                    if (validPos[1]) {
                        break;
                    }
                } else {break;}
            }
        }

        return posMoves;
    }



    public Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece) {
        int[][] direction = new int[][]{{-1,-1}, {-1,0}, {-1,1}, {0,-1},
                {0,1}, {1,-1}, {1,0}, {1,1}};
        return staticMoves(board,myPosition, currPiece, direction);
    }

    public Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece) {
        int[][] direction = new int[][]{{-1,-1}, {-1,0}, {-1,1}, {0,-1},
                {0,1}, {1,-1}, {1,0}, {1,1}};
        return dynamicMoves(board,myPosition, currPiece, direction);
    }

    public Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece) {
        int[][] direction = new int[][]{{-1,-1}, {-1,1}, {1,-1}, {1,1}};
        return dynamicMoves(board,myPosition, currPiece, direction);
    }

    public Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece) {
        int[][] direction = new int[][]{{-1,-2}, {-1,2}, {-2,-1}, {-2,1},
                {1,-2}, {1,2}, {2,-1}, {2,1}};
        return staticMoves(board,myPosition, currPiece, direction);
    }

    public Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece) {
        int[][] direction = new int[][]{{0,-1}, {0,1}, {1,0}, {-1,0}};
        return dynamicMoves(board,myPosition, currPiece, direction);
    }

    public void addPawnMoves(ChessPosition myPosition, ChessPosition posPos, HashSet<ChessMove> posMoves) {
        posMoves.add(new ChessMove(myPosition, posPos, PieceType.QUEEN));
        posMoves.add(new ChessMove(myPosition, posPos, PieceType.BISHOP));
        posMoves.add(new ChessMove(myPosition, posPos, PieceType.KNIGHT));
        posMoves.add(new ChessMove(myPosition, posPos, PieceType.ROOK));
    }

    public Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition, ChessPiece currPiece) {
        HashSet<ChessMove> posMoves = new HashSet<ChessMove>();

        int startingRow = currPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE) ? 2 : 7;
        int endingRow = currPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE) ? 8 : 1;
        int pawnDir = currPiece.getTeamColor().equals(ChessGame.TeamColor.WHITE) ? 1 : -1;

        // moving forward cases
        ChessPosition posPos = new ChessPosition(myPosition.getRow() + pawnDir, myPosition.getColumn());
        Boolean[] validPos = checkValidPos(board, myPosition, currPiece, posPos);
        if (validPos[0] && !validPos[1]) {
            // check if end piece
            if (posPos.getRow() == endingRow) {
                addPawnMoves(myPosition, posPos, posMoves);
            } else {
                posMoves.add(new ChessMove(myPosition, posPos, null));
                if (myPosition.getRow() == startingRow) {
                    posPos = new ChessPosition(myPosition.getRow() + 2*pawnDir, myPosition.getColumn());
                    validPos = checkValidPos(board, myPosition, currPiece, posPos);
                    if (validPos[0] && !validPos[1]) {
                        posMoves.add(new ChessMove(myPosition, posPos, null));
                    }
                }
            }
        }

        // diagonal cases
        for (int i : new int[]{1,-1}) {
            posPos = new ChessPosition(myPosition.getRow() + pawnDir, myPosition.getColumn() + i);
            validPos = checkValidPos(board, myPosition, currPiece, posPos);
            if (validPos[0] && validPos[1]) {
                if (posPos.getRow() == endingRow) {
                    addPawnMoves(myPosition, posPos, posMoves);
                } else {
                    posMoves.add(new ChessMove(myPosition, posPos, null));
                }
            }

        }

        return posMoves;
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

        return switch(currPiece.getPieceType()) {
            case KING -> getKingMoves(board, myPosition, currPiece);
            case QUEEN -> getQueenMoves(board, myPosition, currPiece);
            case BISHOP -> getBishopMoves(board, myPosition, currPiece);
            case KNIGHT -> getKnightMoves(board, myPosition, currPiece);
            case ROOK -> getRookMoves(board, myPosition, currPiece);
            case PAWN -> getPawnMoves(board, myPosition, currPiece);
        };

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
