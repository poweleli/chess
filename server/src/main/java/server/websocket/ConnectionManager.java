package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String userID, Session session) {
        var connection = new Connection(userID, session);
        connections.put(userID, connection);
    }

    public void remove(String userID) {
        connections.remove(userID);
    }
//
//    public void broadcast(String excludeVisitorName, Notification notification) throws IOException {
//        var removeList = new ArrayList<Connection>();
//        for (var c : connections.values()) {
//            if (c.session.isOpen()) {
//                if (!c.visitorName.equals(excludeVisitorName)) {
//                    c.send(notification.toString());
//                }
//            } else {
//                removeList.add(c);
//            }
//        }
//
//        // Clean up any connections that were left open.
//        for (var c : removeList) {
//            connections.remove(c.visitorName);
//        }
//    }
}