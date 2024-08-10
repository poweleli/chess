package server;

import handler.*;
import spark.*;
import server.websocket.WebSocketHandler;

public class Server {
    private final WebSocketHandler webSocketHandler = new WebSocketHandler();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.webSocket("/ws", webSocketHandler);
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", (req,res) -> (new ClearHandler()).handleRequest(req,res));
        Spark.post("/user", (req,res) -> (new RegisterHandler()).handleRequest(req,res));
        Spark.post("/session", (req,res) -> (new LoginHandler()).handleRequest(req,res));
        Spark.delete("/session", (req,res) -> (new LogoutHandler()).handleRequest(req,res));
        Spark.get("/game", (req,res) -> (new ListGamesHandler()).handleRequest(req,res));
        Spark.post("/game", (req,res) -> (new CreateGameHandler()).handleRequest(req,res));
        Spark.put("/game", (req,res) -> (new JoinGameHandler()).handleRequest(req,res));

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
