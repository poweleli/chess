package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    private Gson gson = new Gson();
    public String authToken;
    public Session session;

    public Connection(String authToken, Session session) {
        this.authToken = authToken;
        this.session = session;
    }

    public void send(String msg) throws IOException {
//        session.getRemote().sendString(msg);
        session.getRemote().sendString(gson.toJson(msg));
    }
}
