package client;

import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.ServerMessageHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import model.GameData;
import requests.*;
import responses.*;
import ui.DrawChess;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class ChessClient {
    private String serverUrl;
    private boolean activeApp;
    private final Scanner scanner;
    private final ServerFacade server;
    private WebSocketFacade ws;
    private final ServerMessageHandler serverMessageHandler;
    private State state = State.SIGNEDOUT;
    private String authToken = null;
    private int currGame;

//    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
//    private final HashMap<String, Integer> gameIDs = new HashMap<>();
//    private final HashMap<Integer, String> gameIDs2 = new HashMap<>();

    public ChessClient(String urlString, ServerMessageHandler serverMessageHandler) {
        serverUrl = urlString;
        activeApp = Boolean.TRUE;
        scanner = new Scanner(System.in);
        server = new ServerFacade(urlString);
        this.serverMessageHandler = serverMessageHandler;
    }

    public void start() {
        System.out.println("♕ Welcome to 240 chess. Type Help to get started. ♕");
        while (activeApp) {
            try {
                String userInputMessage = getUserInputMessage();

                System.out.printf("[%s] >>> ", userInputMessage);
                String input = scanner.nextLine();
                String[] inputs = input.split(" ");

                if (inputs[0].equalsIgnoreCase("help")) {
                    getHelp();
                } else if (inputs[0].equalsIgnoreCase("quit")) {
                    activeApp = Boolean.FALSE;
                } else if (inputs[0].equals("CLEAR")) {
                    deleteDB();
                    state = State.SIGNEDOUT;
                } else if (state.equals(State.SIGNEDOUT)) {
                    if (inputs[0].equalsIgnoreCase("register")) {
                        register(inputs);
                        state = State.SIGNEDIN;
                    } else if (inputs[0].equalsIgnoreCase("login")) {
                        login(inputs);
                        state = State.SIGNEDIN;
                    } else {
                        System.out.println("Invalid Request");
                    }
                } else if (state.equals(State.GAMEPLAY)) {
                    if (inputs[0].equalsIgnoreCase("highlight")) {
                        highlight(inputs);
                    } else if (inputs[0].equalsIgnoreCase("move")) {
                        makeMove(inputs);
                    } else if (inputs[0].equalsIgnoreCase("redraw")) {
                        redraw();
                    } else if (inputs[0].equalsIgnoreCase("leave")) {
                        leave();
                        state = State.SIGNEDIN;
                    } else if (inputs[0].equalsIgnoreCase("resign")) {
                        resign();
                    } else {
                        System.out.println("Invalid Request");
                    }

                } else {
                    if (inputs[0].equalsIgnoreCase("create")) {
                        createGame(inputs);
                    } else if (inputs[0].equalsIgnoreCase("list")) {
                        listGame(inputs);
                    } else if (inputs[0].equalsIgnoreCase("join")) {
                        joinGame(inputs);
                        state = State.GAMEPLAY;
                    } else if (inputs[0].equalsIgnoreCase("observe")) {
                        observeGame();
                    } else if (inputs[0].equalsIgnoreCase("logout")) {
                        logout(inputs);
                        state = State.SIGNEDOUT;

                    } else {
                        System.out.println("Invalid Request");
                    }
                }
            } catch (ResponseException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getUserInputMessage() {
        if (state.equals(State.SIGNEDIN)) {
            return "LOGGED_IN";
        } else if (state.equals(State.SIGNEDOUT)) {
            return "LOGGED_OUT";
        } else {
            return "GAMEPLAY";
        }
    }
    public void register(String[] inputs) throws ResponseException{
        if (inputs.length >= 4) {
            RegisterRequest req = new RegisterRequest(inputs[1], inputs[2], inputs[3]);
            RegisterResult res = server.register(req);
            authToken = res.authToken();
            System.out.printf("%s has been registered.%n", res.username());
        } else {
            throw new ResponseException(500, "Expected <USERNAME> <PASSWORD> <EMAIL>");
        }
    }

    public void login(String[] inputs) throws ResponseException{
        if (inputs.length >= 3) {
            LoginRequest req = new LoginRequest(inputs[1], inputs[2]);
            LoginResult res = server.login(req);
            authToken = res.authToken();
            System.out.printf("%s successfully logged in.%n", res.username());
        } else {
            throw new ResponseException(500, "Expected <USERNAME> <PASSWORD>");
        }
    }

    public void logout(String[] inputs) throws ResponseException{
        LogoutRequest req = new LogoutRequest(authToken);
        LogoutResult res = server.logout(req);
        authToken = null;
        System.out.println("Successfully logged out.");
    }

    public void createGame(String[] inputs) throws ResponseException{
        if (inputs.length >= 2) {
            CreateGameRequest req = new CreateGameRequest(authToken, inputs[1]);
            CreateGameResult res = server.createGame(req);
//            String randomID = generateRandomGameID();
//            gameIDs.put(randomID, res.gameID());
//            gameIDs2.put(res.gameID(), randomID);

            System.out.printf("Successfully created \"%s\". Game ID: %s%n", inputs[1], res.gameID());
        } else {
            throw new ResponseException(500, "Expected <NAME>");
        }
    }

    public void printGames(ListGamesResult res) {
        System.out.printf("%-20s %-10s %-15s %-15s%n", "GameName", "GameID", "WhiteUsername", "BlackUsername");
        System.out.println("---------------------------------------------------------------");
        for (GameData game : res.games()) {
            System.out.printf("%-20s %-10s %-15s %-15s%n",
                    game.gameName(),
//                    gameIDs2.get(game.gameID()),
                    game.gameID(),
                    game.whiteUsername(),
                    game.blackUsername()
            );
        }
    }

    public void listGame(String[] inputs) throws ResponseException{
        ListGamesRequest req = new ListGamesRequest(authToken);
        ListGamesResult res = server.listGames(req);
        printGames(res);
    }


    public void highlight(String[] inputs) throws ResponseException {

    }

    public void createWS() throws ResponseException {
        if (ws == null) {
            ws = new WebSocketFacade(serverUrl, serverMessageHandler);
        }
    }

    public void joinGame(String[] inputs) throws ResponseException{
        if (inputs[1] != null && inputs.length >= 3) {
            try {
                JoinGameRequest req = new JoinGameRequest(authToken, inputs[2], Integer.parseInt(inputs[1]));
                JoinGameResult res = server.joinGame(req);
                createWS();
                ws.joinGame(authToken, Integer.parseInt(inputs[1]));
                this.currGame = Integer.parseInt(inputs[1]);
//            showGameBoard();
            } catch (Exception e) {
                throw new ResponseException(500, e.getMessage());
            }
        } else {
            throw new ResponseException(500, "Expected <ID> [WHITE|BLACK]");
        }
    }

    public void makeMove(String[] inputs) throws ResponseException{
        if (inputs.length >= 3) {
            createWS();
            ChessMove move = getChessMove(inputs[1], inputs[2]);
            ws.makeMove(authToken, currGame, move);
        } else {
            throw new ResponseException(500, "Expected <ID> [WHITE|BLACK]");
        }
    }

    public ChessMove getChessMove(String start, String end) throws ResponseException{
        try {
            ChessPosition startPos = convertPosition(start);
            ChessPosition endPos = convertPosition(end);

            return new ChessMove(startPos, endPos, null);
        } catch (Exception e) {
            throw new ResponseException(500, "Error: invalid move.");
        }
    }

    public ChessPosition convertPosition(String position) throws ResponseException {
        try {
            char columnChar = position.charAt(0);
            char rowChar = position.charAt(1);

            int column = Character.toLowerCase(columnChar) - 'a' + 1; // Convert 'A' to 0, 'B' to 1, etc.
            int row = 9 - Character.getNumericValue(rowChar); // Convert '1' to 0, '2' to 1, etc.

            if (column < 0 || column > 7 || row < 0 || row > 7) {
                throw new IllegalArgumentException("Chess position out of bounds.");
            }
            return new ChessPosition(row,column);
        } catch (Exception e) {
            throw new ResponseException(500, "Error: invalid move");
        }
    }



    public void observeGame() throws ResponseException {
//        showGameBoard();
    }

    public void redraw() throws ResponseException {
        createWS();
        ws.redraw();
    }

    public void leave() throws ResponseException {
        createWS();
        ws.leaveGame(authToken, currGame);
    }

    public void resign() throws ResponseException {
        createWS();
        ws.resign(authToken, currGame);
    }



    public void deleteDB() throws ResponseException {
        ClearResult res = server.deleteDB();
        System.out.println("DB has been cleared");
    }




    public void getHelp() {
        if (state.equals(State.SIGNEDIN)) {
            System.out.println("""
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE|BLACK] - a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands
                    """);
        } else if (state.equals(State.GAMEPLAY)) {
            System.out.println("""
                    highlight <COL><ROW> - highlight legal chess moves for piece
                    move <C_COL><C_ROW> <T_COL><T_ROW> - make chess move from current piece to target space
                    redraw - redraws chess board
                    leave - leave chess game
                    resign - forfeit the chess game
                    help - with possible commands
                    """);
        } else {
            System.out.println("""
                    register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                    login <USERNAME> <PASSWORD> - to play chess
                    quit - playing chess
                    help - with possible commands
                    """);
        }

    }

}

