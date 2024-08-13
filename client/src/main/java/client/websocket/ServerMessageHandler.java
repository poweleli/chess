package client.websocket;

import chess.ChessPosition;
import model.GameData;
import websocket.messages.ServerMessage;

public interface ServerMessageHandler {
    void notify(ServerMessage serverMessage);
    void getBoard(int gameID, GameData data, ChessPosition highlightPos);
}
