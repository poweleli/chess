package ui;

import chess.ChessGame;
import chess.ChessPiece;
import client.ServerFacade;

import java.util.Scanner;

public class Console {
    boolean activeApp;
    boolean loggedIn;
    Scanner scanner;
//    private final ServerFacade server;

    public Console() {
        activeApp = Boolean.TRUE;
        loggedIn = Boolean.FALSE;
        scanner = new Scanner(System.in);

//        server

    }

    public void start() {
        System.out.println("♕ Welcome to 240 chess. Type Help to get started. ♕");
        while (activeApp) {
            String userInputMessage = loggedIn ? "LOGGED_IN" : "LOGGED_OUT";
            System.out.printf("[%s] >>> ", userInputMessage);
            String input = scanner.nextLine();
            String[] inputs = input.split(" ");

            if (inputs[0].equalsIgnoreCase("help")) {
                getHelp(loggedIn);
            } else if (inputs[0].equalsIgnoreCase("quit")) {
                activeApp = Boolean.FALSE;
            } else if (!loggedIn) {
                if (inputs[0].equalsIgnoreCase("register")) {
                    //                    register(input);
                    System.out.println("register");
                } else if (inputs[0].equalsIgnoreCase("login")) {
                    //                    login(input);
                    System.out.println("login");
                    loggedIn = Boolean.TRUE;
                } else {
                    System.out.println("Invalid Request");
                }
            } else {
                if (inputs[0].equalsIgnoreCase("create")) {
                    //                    createGame();
                    System.out.println("create");
                } else if (inputs[0].equalsIgnoreCase("list")) {
                    //                    listGame();
                    System.out.println("list");
                } else if (inputs[0].equalsIgnoreCase("join")) {
                    //                    joinGame();
                    System.out.println("join");
                } else if (inputs[0].equalsIgnoreCase("observe")) {
                    //                    observeGame();
                    System.out.println("observe");
                } else if (inputs[0].equalsIgnoreCase("logout")) {
                    //                    logout();
                    System.out.println("logout");
                    loggedIn = Boolean.FALSE;

                } else {
                    System.out.println("Invalid Request");
                }
            }

        }

//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
    }

    public static void getHelp(boolean loggedIn) {
        if (loggedIn) {
            System.out.println("""
                    create <NAME> - a game
                    list - games
                    join <ID>[WHITE|BLACK] - a game
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

