import client.ChessClient;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        ChessClient console = new ChessClient(serverUrl);
        console.start();
    }
}