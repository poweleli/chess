package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
//        Spark.post("/user", (req,res) -> (new RegisterHandler()).handleRequest(req,res));
        Spark.post("/user", (req,res) -> "Hello BYU!");


        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object registerUser(Request request, Response response) {
        System.out.println("Testing register user");
        return null;
    }
}
