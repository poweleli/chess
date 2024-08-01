package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.*;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import requests.*;
import responses.*;

import java.util.Collection;

public class GameServiceTests {
    private static final GameService GAME_SERVICE;
    private static final UserService USER_SERVICE;
    private AuthData authData;

    static {
        try {
            GAME_SERVICE = new GameService();
            USER_SERVICE = new UserService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        GAME_SERVICE.clear();
        USER_SERVICE.clear();
        RegisterRequest regReq = new RegisterRequest("username", "password", "email");
        ResultInterface regRes = USER_SERVICE.register(regReq);
        this.authData = new AuthData("username", ((RegisterResult) regRes).authToken());
    }

    //  List Game
    @Test
    @DisplayName("List Games")
    public void listGameSuccess() throws Exception {
        CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
        ResultInterface res = GAME_SERVICE.createGame(req);

        ListGamesRequest req1 = new ListGamesRequest(authData.authToken());
        ResultInterface res1 = GAME_SERVICE.listGame(req1);

        Assertions.assertTrue(res1 instanceof ListGamesResult, "Result wrong type");
    }

    @Test
    @DisplayName("List Games Bad Auth")
    public void listGameFailure() throws Exception {
        CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
        ResultInterface res = GAME_SERVICE.createGame(req);

        ListGamesRequest req1 = new ListGamesRequest("1234");
        ResultInterface res1 = GAME_SERVICE.listGame(req1);

        Assertions.assertTrue(res1 instanceof ErrorResult, "Result wrong type");
        Assertions.assertEquals(((ErrorResult) res1).message(), "Error: unauthorized", "Wrong error message");
    }

    // Create Game Tests
    @Test
    @DisplayName("Create Game")
    public void createGameSuccess() throws Exception {
        CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
        ResultInterface res = GAME_SERVICE.createGame(req);

        if (res instanceof ErrorResult) {
            System.out.println(((ErrorResult) res).message());
        }
        Assertions.assertTrue(res instanceof CreateGameResult, "Result wrong type");
    }

    @Test
    @DisplayName("Create Game No Game Name")
    public void createGameFailure() throws Exception {
        CreateGameRequest req = new CreateGameRequest(authData.authToken(), "");
        ResultInterface res = GAME_SERVICE.createGame(req);

        Assertions.assertTrue(res instanceof ErrorResult, "Result wrong type");
        Assertions.assertEquals(((ErrorResult) res).message(), "Error: bad request", "Wrong error message");

    }

    // Join Game Tests
    @Test
    @DisplayName("Join Game")
    public void joinGameSuccess() throws Exception {
        CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
        ResultInterface res = GAME_SERVICE.createGame(req);

        int gameID = ((CreateGameResult) res).gameID();

        JoinGameRequest req1 = new JoinGameRequest(authData.authToken(), "WHITE", gameID);
        ResultInterface res1 = GAME_SERVICE.joinGame(req1);

        Assertions.assertTrue(res1 instanceof JoinGameResult, "Result wrong type");
    }

    @Test
    @DisplayName("Join Game Invalid Color")
    public void joinGameFailure() throws Exception {
        CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
        ResultInterface res = GAME_SERVICE.createGame(req);
        int gameID = ((CreateGameResult) res).gameID();

        JoinGameRequest req1 = new JoinGameRequest(authData.authToken(), "BLUE", gameID);
        ResultInterface res1 = GAME_SERVICE.joinGame(req1);

        Assertions.assertTrue(res1 instanceof ErrorResult, "Result wrong type");
        Assertions.assertEquals(((ErrorResult) res1).message(), "Error: bad request","Result wrong type");
    }

    // Clear Game Test
    @Test
    @DisplayName("Clear Game")
    public void clearGame() throws Exception {
        CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
        ResultInterface res = GAME_SERVICE.createGame(req);

        ResultInterface res1 = GAME_SERVICE.clear();

        JoinGameRequest req2 = new JoinGameRequest(authData.authToken(), "BLUE", '1');
        ResultInterface res2 = GAME_SERVICE.joinGame(req2);

        Assertions.assertTrue(res2 instanceof ErrorResult, "Result wrong type");
        Assertions.assertEquals(((ErrorResult) res2).message(), "Error: bad request","Result wrong type");

    }

}
