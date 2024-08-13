package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import client.websocket.ServerMessageHandler;
import model.GameData;
import ui.DrawChess;
import websocket.messages.ServerMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements ServerMessageHandler {
    private final ChessClient client;
    private Map<Integer,GameData> latestData = new HashMap<>();

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        client.start();
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(serverMessage.getMessage());
    }

    @Override
    public void getBoard(int gameID, GameData data, ChessPosition highlightPos) {
        if (data != null) {
            latestData.put(gameID, data);
        }
        ChessGame game = latestData.get(gameID).game();
        DrawChess printer = new DrawChess();
        printer.drawBoard(game, highlightPos);
    }
}