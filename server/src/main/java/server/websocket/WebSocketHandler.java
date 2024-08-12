package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.*;
import dataaccess.AuthSQL;
import dataaccess.DataAccessException;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import requests.CreateGameRequest;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;
import model.*;
import chess.ChessGame.TeamColor;
import server.Server;

import javax.management.Notification;
import java.io.IOException;
import java.util.*;


@WebSocket
public class WebSocketHandler {
    private final Gson gson = new Gson();
    private final Map<Integer, Set<Session>> connections = new HashMap<>();
    private GameService gameService;
    private UserService userService;
    private GameData gameData;
    private AuthData authData;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand action = gson.fromJson(message, UserGameCommand.class);
            checkValidAction(action, session);

            switch (action.getCommandType()) {
                case CONNECT -> connect(action, session);
                case MAKE_MOVE -> makeMove(gson.fromJson(message, MakeMoveCommand.class), session);
                case LEAVE -> leave(action, session);
                case RESIGN -> resign(action, session);

            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void checkValidAction(UserGameCommand action, Session session) throws Exception {
        try {
            gameService = new GameService();
            userService = new UserService();
            authData = userService.getAuthUser(action.getAuthString());
            gameData = gameService.getGame(action.getGameID());
        } catch (Exception e) {
            sendMessage(session, new ErrorMessage("Error: invalid request."));
            throw new ResponseException(500, "error");
        }
    }

    private void connect(UserGameCommand action, Session session) throws Exception {
        try {
            connections.computeIfAbsent(action.getGameID(), k -> new HashSet<>()).add(session);
            NotificationMessage groupMessage = getConnectNotification(action);
            broadcast(action.getGameID(), groupMessage, session);

            LoadGameMessage gameMessage = new LoadGameMessage(gameData);
            sendMessage(session, gameMessage);
        } catch (Exception e) {
            sendMessage(session, new ErrorMessage(e.getMessage()));
        }
    }

    private void makeMove(MakeMoveCommand action, Session session) throws IOException {
        try {
            TeamColor userColor = getUserColor();
            if (userColor == null) {
                throw new ResponseException(500, String.format("Error: %s is an observer.",authData.username()));
            }
            TeamColor otherColor = userColor.equals(TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
            if (!userColor.equals(gameData.game().getTeamTurn())) {
                throw new ResponseException(500, "Error: invalid move");
            }
            if (gameData.game().gameOver()) {
                throw new ResponseException(500, "Error: game over");
            }

            gameData.game().makeMove(action.getChessMove());
            gameData = gameService.updateGame(action.getGameID(), gameData.game());
            sendMessageAll(action.getGameID(), new LoadGameMessage(gameData));
            broadcast(action.getGameID(), new NotificationMessage(action.getChessMove().toString()), session);

            if (gameData.game().isInCheckmate(otherColor)) {
                sendMessageAll(action.getGameID(), new NotificationMessage(String.format("%s is in checkmate.", otherColor)));
            } else if (gameData.game().isInStalemate(otherColor)) {
                sendMessageAll(action.getGameID(), new NotificationMessage(String.format("%s is in stalemate.", otherColor)));
            } else if (gameData.game().isInCheck(otherColor) ) {
                sendMessageAll(action.getGameID(), new NotificationMessage(String.format("%s is in check.", otherColor)));
            }

        } catch (Exception e) {
            sendMessage(session, new ErrorMessage(e.getMessage()));
        }
    }

    private void leave(UserGameCommand action, Session session) throws IOException{
        try {
            connections.get(action.getGameID()).remove(session);
            TeamColor userColor = getUserColor();
            if (userColor != null) {
                gameService.removePlayer(userColor, action.getGameID());
            }
            sendMessageAll(action.getGameID(), new NotificationMessage(String.format("%s has left the game", authData.username())));
        } catch (Exception e) {
            sendMessage(session, new ErrorMessage(e.getMessage()));
        }
    }

    private void resign(UserGameCommand action, Session session) throws IOException {
        try {
            TeamColor userColor = getUserColor();
            if (!gameData.game().gameOver()) {
                if (userColor == null) {
                    throw new ResponseException(500, "Error: cannot resign as observer");
                }
                gameService.setGameOver(action.getGameID());
                sendMessageAll(action.getGameID(), new NotificationMessage(String.format("%s has resigned", authData.username())));
            } else {
                throw new ResponseException(500, "Error: game is already over");
            }

        } catch (Exception e) {
            sendMessage(session, new ErrorMessage(e.getMessage()));
        }

    }

    public void sendMessage(Session session, ServerMessage message) throws IOException {
        if (session.isOpen()) {
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }

    private void broadcast(int gameID, ServerMessage message, Session currSession) throws Exception {
        Set<Session> sessions = connections.get(gameID);
        if (sessions != null) {
            for (var s : sessions) {
                if (s != currSession) {
                    sendMessage(s, message);
                }
            }
        }
        cleanupConnections();
    }

    private void sendMessageAll(int gameID, ServerMessage message) throws Exception {
        Set<Session> sessions = connections.get(gameID);
        if (sessions != null) {
            for (var s : sessions) {
                sendMessage(s, message);
            }
        }

        cleanupConnections();
    }

    public void cleanupConnections() {
        for (Map.Entry<Integer, Set<Session>> entry : connections.entrySet()) {
            entry.getValue().removeIf(session -> !session.isOpen());
        }
        connections.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }


    private TeamColor getUserColor() throws Exception {
//         check if observer
        if (authData.username().equals(gameData.blackUsername())) {
           return TeamColor.BLACK;
        } else if (authData.username().equals(gameData.whiteUsername())) {
            return TeamColor.WHITE;
        } else {
            return null;
        }
    }

    private NotificationMessage getConnectNotification(UserGameCommand action) throws ResponseException {
        String message;
        if (Objects.equals(gameData.blackUsername(), authData.username())) {
            message = String.format("%s is joining the game as %s player", authData.username(), "black");
        } else if (Objects.equals(gameData.whiteUsername(), authData.username())) {
            message = String.format("%s is joining the game as %s player", authData.username(), "white");
        } else {
            message = String.format("%s is joining the game as an observer", authData.username());
        }
        return new NotificationMessage(message);
    }

}