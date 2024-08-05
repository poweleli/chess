package client;

import client.ServerFacade;
import exception.ResponseException;
import requests.*;
import responses.*;

import java.util.Scanner;

public class ChessClient {
    private boolean activeApp;
    private Scanner scanner;
    private ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken = null;
//    private final ServerFacade server;

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
                } else if (state.equals(State.SIGNEDOUT)) {
                    if (inputs[0].equalsIgnoreCase("register")) {
                        System.out.println(register(inputs));
                        state = State.SIGNEDIN;
                    } else if (inputs[0].equalsIgnoreCase("login")) {
                        System.out.println(login(inputs));
                        state = State.SIGNEDIN;
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
                        System.out.println(logout(inputs));
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

    public String register(String[] inputs) throws ResponseException{
        if (inputs.length >= 4) {
            RegisterRequest req = new RegisterRequest(inputs[1], inputs[2], inputs[3]);
            RegisterResult res = server.register(req);
            authToken = res.authToken();
            return String.format("%s has been registered.", res.username());
        }
        throw new ResponseException(500, "Expected <USERNAME> <PASSWORD> <EMAIL>");
    }

    public String login(String[] inputs) throws ResponseException{
        if (inputs.length >= 3) {
            LoginRequest req = new LoginRequest(inputs[1], inputs[2]);
            LoginResult res = server.login(req);
            authToken = res.authToken();
            return String.format("%s successfully logged in.", res.username());
        }
        throw new ResponseException(500, "Expected <USERNAME> <PASSWORD>");
    }

    public String logout(String[] inputs) throws ResponseException{
        LogoutRequest req = new LogoutRequest(authToken);
        LogoutResult res = server.logout(req);
        authToken = null;
        return "Successfully logged out.";
    }


    public void getHelp() {
        if (state.equals(State.SIGNEDIN)) {
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

