package client.websocket;

import model.GameData;
import websocket.messages.ServerMessage;

public interface ServerMessageHandler {
    void notify(ServerMessage serverMessage);
    void updateBoard(GameData latestData);
}