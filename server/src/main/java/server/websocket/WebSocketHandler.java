package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import model.*;

import javax.management.Notification;
import java.io.IOException;
import java.util.*;


@WebSocket
public class WebSocketHandler {
    private final Gson gson = new Gson();
    private final Map<Integer,Set<Session>> connections = new HashMap<>();
    private WebSocketService webSocketService;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand action = gson.fromJson(message, UserGameCommand.class);
            webSocketService = new WebSocketService();
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

    private void connect(UserGameCommand action, Session session) throws Exception {
        if (validAction(action)) {
            Set<Session> currSessions = connections.get(action.getGameID());
            connections.computeIfAbsent(action.getGameID(), k -> new HashSet<>()).add(session);

            if (currSessions != null) {
                NotificationMessage groupMessage = getConnectNotification(action);
                for (var s : currSessions) {
                    if (s != session) {
                        sendMessage(s, groupMessage);
                    }
                }
            }

            LoadGameMessage gameMessage = getGameMessage(action);
            sendMessage(session, gameMessage);
        }
        else {
            sendMessage(session, new ErrorMessage("Error: invalid request."));
        }
    }

    private Boolean validAction(UserGameCommand action) {
        try {
            webSocketService.getGameData(action.getGameID());
            webSocketService.getUser(action.getAuthString());
            return Boolean.TRUE;
        } catch(Exception e) {
            return Boolean.FALSE;
        }
    }

    private LoadGameMessage getGameMessage(UserGameCommand action) throws Exception {
        GameData gameData = webSocketService.getGameData(action.getGameID());
        return new LoadGameMessage(gameData);
    }

    private NotificationMessage getConnectNotification(UserGameCommand action) throws Exception {
        GameData gameData = webSocketService.getGameData(action.getGameID());
        AuthData authData = webSocketService.getUser(action.getAuthString());

        String message;
        if (Objects.equals(gameData.blackUsername(), authData.username())) {
            message = String.format("%s is joining the game as %s player", authData.username(), "black");
        } else if (Objects.equals(gameData.whiteUsername(), authData.username())) {
            message = String.format("%s is joining the game as %s player", authData.username(), "white");
        } else {
            message = String.format("%s is joining the game as an observer", authData.username());
        }
        return new NotificationMessage(message);
    }

    private void makeMove(UserGameCommand action, Session session) {

    }

    private void leave(UserGameCommand action, Session session) {

    }

    private void resign(UserGameCommand action, Session session) {

    }

    public void sendMessage(Session session, ServerMessage message) throws IOException {
        session.getRemote().sendString(new Gson().toJson(message));
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