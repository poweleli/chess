package websocket.messages;

import chess.ChessGame;

public class ErrorMessage extends ServerMessage {
    String errorMessage;

    public ErrorMessage(String errorMessage) {
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }
}
