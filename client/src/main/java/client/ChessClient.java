package client;

import exception.ResponseException;
import model.GameData;
import requests.*;
import responses.*;
import ui.DrawChess;

import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class ChessClient {
    private boolean activeApp;
    private final Scanner scanner;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken = null;
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private HashMap<String, Integer> gameIDs = new HashMap<>();
    private HashMap<Integer, String> gameIDs2 = new HashMap<>();

    public ChessClient(String urlString) {
        activeApp = Boolean.TRUE;
        scanner = new Scanner(System.in);
        server = new ServerFacade(urlString);
    }

    public void start() {
        System.out.println("♕ Welcome to 240 chess. Type Help to get started. ♕");
        while (activeApp) {
            try {
                String userInputMessage = state.equals(State.SIGNEDIN) ? "LOGGED_IN" : "LOGGED_OUT";
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
                } else {
                    if (inputs[0].equalsIgnoreCase("create")) {
                        createGame(inputs);
                    } else if (inputs[0].equalsIgnoreCase("list")) {
                        listGame(inputs);
                    } else if (inputs[0].equalsIgnoreCase("join")) {
                        joinGame(inputs);
                        System.out.println("join");
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

    public String generateRandomGameID() {
        Random random = new Random();
        StringBuilder gameID = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            gameID.append(ALPHANUMERIC.charAt(random.nextInt(ALPHANUMERIC.length())));
        }
        return gameID.toString();
    }
    public void createGame(String[] inputs) throws ResponseException{
        if (inputs.length >= 2) {
            CreateGameRequest req = new CreateGameRequest(authToken, inputs[1]);
            CreateGameResult res = server.createGame(req);
            String randomID = generateRandomGameID();
            gameIDs.put(randomID, res.gameID());
            gameIDs2.put(res.gameID(), randomID);

            System.out.printf("Successfully created \"%s\". Game ID: %s%n", inputs[1], randomID);
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
                    gameIDs2.get(game.gameID()),
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

    public void showGameBoard() {
        System.out.println("Game Board");
        String[] args = new String[]{"TEST"};
        DrawChess.main(args);
    }

    public void joinGame(String[] inputs) throws ResponseException{
        try { gameIDs.get(inputs[1]); } catch (Exception e) {throw new ResponseException(500, "Expected <ID> [WHITE|BLACK]");}
        if (inputs.length >= 3) {
            JoinGameRequest req = new JoinGameRequest(authToken, inputs[2], gameIDs.get(inputs[1]));
            JoinGameResult res = server.joinGame(req);
            showGameBoard();
        } else {
            throw new ResponseException(500, "Expected <ID> [WHITE|BLACK]");
        }
    }

    public void observeGame() throws ResponseException {
        showGameBoard();
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

