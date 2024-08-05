import client.ChessClient;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";

        ChessClient console = new ChessClient(serverUrl);
        console.start();
    }
}