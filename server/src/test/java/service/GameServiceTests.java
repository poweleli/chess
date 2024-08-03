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
        try {
            GAME_SERVICE.clear();
            USER_SERVICE.clear();
            RegisterRequest regReq = new RegisterRequest("username", "password", "email");
            RegisterResult regRes = USER_SERVICE.register(regReq);
            this.authData = new AuthData("username", ((RegisterResult) regRes).authToken());
        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, "Unable to setup.");
        }
    }

    //  List Game
    @Test
    @DisplayName("List Games")
    public void listGameSuccess() {
        try {
            CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
            CreateGameResult res = GAME_SERVICE.createGame(req);

            ListGamesRequest req1 = new ListGamesRequest(authData.authToken());
            ListGamesResult res1 = GAME_SERVICE.listGame(req1);

            Assertions.assertNotNull(res1, "Result wrong type");
        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("List Games Bad Auth")
    public void listGameFailure() {
        try {
            CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
            CreateGameResult res = GAME_SERVICE.createGame(req);

            ListGamesRequest req1 = new ListGamesRequest("1234");
            ListGamesResult res1 = GAME_SERVICE.listGame(req1);
        } catch (Exception e) {
            Assertions.assertEquals("Error: unauthorized", e.getMessage());
        }
    }

    // Create Game Tests
    @Test
    @DisplayName("Create Game")
    public void createGameSuccess() {
        try {
            CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
            CreateGameResult res = GAME_SERVICE.createGame(req);

            Assertions.assertNotNull(res, "Result wrong type");
        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Create Game No Game Name")
    public void createGameFailure() {
        try {
            CreateGameRequest req = new CreateGameRequest(authData.authToken(), "");
            CreateGameResult res = GAME_SERVICE.createGame(req);
        } catch (Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }

    }

    // Join Game Tests
    @Test
    @DisplayName("Join Game")
    public void joinGameSuccess() {
        try {
            CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
            CreateGameResult res = GAME_SERVICE.createGame(req);

            int gameID = res.gameID();

            JoinGameRequest req1 = new JoinGameRequest(authData.authToken(), "WHITE", gameID);
            JoinGameResult res1 = GAME_SERVICE.joinGame(req1);

            Assertions.assertNotNull(res1, "Result wrong type");
        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Join Game Invalid Color")
    public void joinGameFailure() {
        try {
            CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
            CreateGameResult res = GAME_SERVICE.createGame(req);
            int gameID = res.gameID();

            JoinGameRequest req1 = new JoinGameRequest(authData.authToken(), "BLUE", gameID);
            JoinGameResult res1 = GAME_SERVICE.joinGame(req1);

        } catch (Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }
    }

    // Clear Game Test
    @Test
    @DisplayName("Clear Game")
    public void clearGame() {
        try {
            CreateGameRequest req = new CreateGameRequest(authData.authToken(), "Game_1");
            CreateGameResult res = GAME_SERVICE.createGame(req);

            ClearResult res1 = GAME_SERVICE.clear();

            JoinGameRequest req2 = new JoinGameRequest(authData.authToken(), "BLUE", '1');
            JoinGameResult res2 = GAME_SERVICE.joinGame(req2);

        } catch (Exception e) {
            Assertions.assertEquals("Error: bad request", e.getMessage());
        }

    }

}
