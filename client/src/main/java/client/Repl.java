package client;

import client.websocket.ServerMessageHandler;
import model.GameData;
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
        System.out.println(SET_TEXT_COLOR_RED + serverMessage.getMessage());
    }

    @Override
    public void updateBoard(GameData latestData) {
        System.out.println("UPDATING BOARD HERE");
    }
}