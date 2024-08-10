package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage {
    ChessGame gameState;

    public LoadGameMessage(ServerMessageType type, ChessGame gameState) {
        super(type);
        this.gameState = gameState;
    }

    public ChessGame getGameState() {
        return gameState;
    }
}
