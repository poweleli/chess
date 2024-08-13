package client.websocket;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.ServerMessageHandler;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    private final Gson gson = new Gson();
    ServerMessageHandler serverMessageHandler;
    Session session;

    public WebSocketFacade(String url, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case NOTIFICATION -> serverMessageHandler.notify(gson.fromJson(message, NotificationMessage.class));
                        case ERROR ->  serverMessageHandler.notify(gson.fromJson(message, ErrorMessage.class));
                        case LOAD_GAME -> {
                            LoadGameMessage loadGameMessage = gson.fromJson(message, LoadGameMessage.class);
                            GameData latestData = loadGameMessage.getGame();
                            serverMessageHandler.getBoard(latestData.gameID(), latestData, null);
                        }
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void joinGame(String authToken, int gameID) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {
            throw new ResponseException(500, "Error: join game failure");
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        try {
            UserGameCommand command = new MakeMoveCommand(MakeMoveCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {
            throw new ResponseException(500, "Error: make move failure");
        }
    }

    public void redraw(int gameID) throws ResponseException {
        try {
            serverMessageHandler.getBoard(gameID, null, null);
        } catch (Exception e) {
            throw new ResponseException(500, "Error: redraw failure");
        }
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {
            throw new ResponseException(500, "Error: redraw failure");
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        try {
            UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (Exception e) {
            throw new ResponseException(500, "Error: resign failure");
        }
    }

    public void highlight(int gameID, ChessPosition pos) throws ResponseException {
        try {
            serverMessageHandler.getBoard(gameID,null, pos);
        } catch (Exception e) {
            throw new ResponseException(500, "Error: highlight failure");
        }
    }
}
