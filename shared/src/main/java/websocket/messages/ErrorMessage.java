package websocket.messages;

import chess.ChessGame;

public class ErrorMessage extends ServerMessage {
    String errorMessage;

    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
