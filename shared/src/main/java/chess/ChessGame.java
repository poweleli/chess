package chess;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurnColor;
    ChessBoard gameBoard;
    Boolean gameOver = Boolean.FALSE;

    public ChessGame() {
        this.gameBoard = new ChessBoard();
        teamTurnColor = TeamColor.WHITE;
        gameBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurnColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurnColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public Boolean checkLegalMove(ChessMove posMove) {
        // make possible move
        ChessPiece movingPiece = gameBoard.getPiece(posMove.getStartPosition());
        ChessPiece upgradedPiece = posMove.getPromotionPiece()==null ? movingPiece : new ChessPiece(teamTurnColor, posMove.getPromotionPiece());
        ChessPiece takenPiece = gameBoard.getPiece(posMove.getEndPosition());

        // move piece
        gameBoard.addPiece(posMove.getStartPosition(), null);
        gameBoard.addPiece(posMove.getEndPosition(), upgradedPiece);

        // check if your king is in danger
        Boolean isValid = !isInCheck(movingPiece.getTeamColor());

        // undo move
        gameBoard.addPiece(posMove.getStartPosition(), movingPiece);
        gameBoard.addPiece(posMove.getEndPosition(), takenPiece);

        return isValid;
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        HashSet<ChessMove> validMoves = new HashSet<ChessMove>();
        ChessPiece startPiece = gameBoard.getPiece(startPosition);
        if (startPiece != null) {
            Collection<ChessMove> pieceMoves = startPiece.pieceMoves(gameBoard, startPosition);
            for (ChessMove move : pieceMoves) {
                if (checkLegalMove(move)) {
                    validMoves.add(move);
                }
            }
        } else {return null;}
        return validMoves;
    }

    public TeamColor teamSwitch() {
        if (teamTurnColor.equals(TeamColor.WHITE)) {
            return TeamColor.BLACK;
        } else {
            return TeamColor.WHITE;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece movingPiece = move.getPromotionPiece()==null ? gameBoard.getPiece(move.getStartPosition()) :
                new ChessPiece(gameBoard.getPiece(move.getStartPosition()).getTeamColor(), move.getPromotionPiece());

        if (!gameOver &&
            movingPiece != null && // check if piece is null
            movingPiece.getTeamColor().equals(teamTurnColor) && // check if piece's turn
            checkLegalMove(move) && // check if the move is legal
            validMoves(move.getStartPosition()).contains(move) // check if the move is valid
        ) {
                gameBoard.addPiece(move.getStartPosition(), null);
                gameBoard.addPiece(move.getEndPosition(), movingPiece);
                setTeamTurn(teamSwitch());
        } else {throw new InvalidMoveException("Error: invalid move.");}
    }

    public ChessPosition findPiecePos(TeamColor teamColor, ChessPiece.PieceType pieceType) {
        for (int i=1; i<=8; i++) {
            for (int j=1; j<=8; j++) {
                ChessPosition posPos = new ChessPosition(i,j);
                ChessPiece posPiece = gameBoard.getPiece(posPos);
                if (posPiece != null) {
                    if (posPiece.getPieceType().equals(pieceType) && posPiece.getTeamColor().equals(teamColor)) {
                        return posPos;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // get the king move
        ChessPosition kingPos = findPiecePos(teamColor, ChessPiece.PieceType.KING);

        // iterate all the possible pieces on board and see if the King is in check
        for (int i=1; i<=8; i++) {
            for (int j=1; j<=8; j++) {
                ChessPosition posPos = new ChessPosition(i,j);
                ChessPiece posPiece = gameBoard.getPiece(posPos);
                if (checkCheck(teamColor, posPos, posPiece, kingPos)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    public boolean checkCheck(TeamColor teamColor, ChessPosition posPos, ChessPiece posPiece, ChessPosition kingPos) {
        if (posPiece != null && !posPiece.getTeamColor().equals(teamColor)) {
            for (ChessMove posMove : posPiece.pieceMoves(gameBoard, posPos)) {
                if (posMove.getEndPosition().equals(kingPos)) {
                    return Boolean.TRUE;
                }
            }
        }
        return Boolean.FALSE;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // iterate all the possible pieces on board and see if there are any legal moves
        for (int i=1; i<=8; i++) {
            for (int j=1; j<=8; j++) {
                ChessPosition posPos = new ChessPosition(i,j);
                ChessPiece posPiece = gameBoard.getPiece(posPos);
                if (posPiece != null &&
                    posPiece.getTeamColor().equals(teamColor) &&
                    !validMoves(posPos).isEmpty()) {

                    return Boolean.FALSE;
                }
            }
        }
        return Boolean.TRUE;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && isInCheckmate(teamColor);
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

    public void setGameOver() {
        gameOver = Boolean.TRUE;
    }

    public Boolean gameOver() {return gameOver;}

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()) {return false;}
        ChessGame chessGame = (ChessGame) o;
        return teamTurnColor == chessGame.teamTurnColor && Objects.equals(gameBoard, chessGame.gameBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurnColor, gameBoard);
    }
}
