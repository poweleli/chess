package client;

import websocket.messages.ServerMessage;

public class NotificationHandler {
    public void notify(ServerMessage message) {
        System.out.println(message.getMessage());
    }
}
