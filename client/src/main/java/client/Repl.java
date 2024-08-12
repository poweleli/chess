package client;

import chess.ChessBoard;
import client.websocket.ServerMessageHandler;
import model.GameData;
import ui.DrawChess;
import websocket.messages.ServerMessage;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl implements ServerMessageHandler {
    private final ChessClient client;

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
    public void getBoard(GameData latestData) {
        ChessBoard board = latestData.game().getBoard();
        DrawChess printer = new DrawChess();
        printer.drawBoard(board);
    }
}