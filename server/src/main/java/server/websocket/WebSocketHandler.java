package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final Gson gson = new Gson();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand action = gson.fromJson(message, UserGameCommand.class);
            switch (action.getCommandType()) {
                case CONNECT -> connect(action, session);
                case MAKE_MOVE -> makeMove(action, session);
                case LEAVE -> leave(action, session);
                case RESIGN -> resign(action, session);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void connect(UserGameCommand action, Session session) throws IOException{
        connections.add(action.getAuthString(), session);
        System.out.println("added successfully");
        var message = String.format("%s is in the shop", "user"); //TODO: fix this to name
        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(notification);
    }

    private void makeMove(UserGameCommand action, Session session) {

    }

    private void leave(UserGameCommand action, Session session) {

    }

    private void resign(UserGameCommand action, Session session) {

    }

//    private void enter(String visitorName, Session session) throws IOException {
//        connections.add(visitorName, session);
//        var message = String.format("%s is in the shop", visitorName);
//        var notification = new Notification(Notification.Type.ARRIVAL, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    private void exit(String visitorName) throws IOException {
//        connections.remove(visitorName);
//        var message = String.format("%s left the shop", visitorName);
//        var notification = new Notification(Notification.Type.DEPARTURE, message);
//        connections.broadcast(visitorName, notification);
//    }
//
//    public void makeNoise(String petName, String sound) throws ResponseException {
//        try {
//            var message = String.format("%s says %s", petName, sound);
//            var notification = new Notification(Notification.Type.NOISE, message);
//            connections.broadcast("", notification);
//        } catch (Exception ex) {
//            throw new ResponseException(500, ex.getMessage());
//        }
//    }
}