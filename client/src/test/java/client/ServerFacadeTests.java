package client;

import org.junit.jupiter.api.*;
import requests.*;
import responses.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:8080");
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @BeforeEach
    public void setup() {
        try {
            facade.deleteDB();
        } catch (Exception e ) {
            System.out.println("Error in deleting db");
        }
    }

    @Test
    @DisplayName("Register Success")
    public void registerSuccess() {
        try {
            RegisterRequest req = new RegisterRequest("p", "p", "p");
            RegisterResult res = facade.register(req);
            Assertions.assertTrue(res.authToken().length() >= 10, "Auth not returned");

        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Register Invalid Username")
    public void registerFail() {
        try {
            RegisterRequest req = new RegisterRequest("", "p", "");
            RegisterResult res = facade.register(req);
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: bad request");
        }
    }

    @Test
    @DisplayName("Login Success")
    public void loginSuccess() {
        try {
            RegisterRequest req = new RegisterRequest("p", "p", "p");
            facade.register(req);
            LoginRequest req1 = new LoginRequest("p", "p");
            LoginResult res = facade.login(req1);

            Assertions.assertTrue(res.authToken().length() >= 10, "Auth not returned");

        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Login Without Register")
    public void loginFail() {
        try {
            LoginRequest req = new LoginRequest("username", "password");
            facade.login(req);
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized");
        }
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGameSuccess() {
        try {
            RegisterRequest req = new RegisterRequest("p", "p", "p");
            RegisterResult res = facade.register(req);
            CreateGameRequest req1 = new CreateGameRequest(res.authToken(), "gameName");
            CreateGameResult res1 = facade.createGame(req1);

            Assertions.assertNotNull(res1, "Auth not returned");

        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Create Game without Logging In")
    public void createGameFail() {
        try {
            CreateGameRequest req1 = new CreateGameRequest("123234;alkjfdasldjkfq", "gameName");
            CreateGameResult res1 = facade.createGame(req1);
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized");
        }
    }

    @Test
    @DisplayName("Join Game Success")
    public void joinGameSuccess() {
        try {
            RegisterRequest req = new RegisterRequest("p", "p", "p");
            RegisterResult res = facade.register(req);
            CreateGameRequest req1 = new CreateGameRequest(res.authToken(), "gameName");
            CreateGameResult res1 = facade.createGame(req1);
            JoinGameRequest req2 = new JoinGameRequest(res.authToken(), "WHITE", res1.gameID());
            JoinGameResult res2 = facade.joinGame(req2);

            Assertions.assertNotNull(res2, "Auth not returned");

        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Join Game Invalid Color")
    public void joinGameFail() {
        try {
            RegisterRequest req = new RegisterRequest("p", "p", "p");
            RegisterResult res = facade.register(req);
            CreateGameRequest req1 = new CreateGameRequest(res.authToken(), "gameName");
            CreateGameResult res1 = facade.createGame(req1);
            JoinGameRequest req2 = new JoinGameRequest(res.authToken(), "BLUE", res1.gameID());
            JoinGameResult res2 = facade.joinGame(req2);
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: bad request");
        }
    }

    @Test
    @DisplayName("Logout Success")
    public void logoutSuccess() {
        try {
            RegisterRequest req = new RegisterRequest("p", "p", "p");
            RegisterResult res = facade.register(req);
            LogoutRequest req2 = new LogoutRequest(res.authToken());
            LogoutResult res2 = facade.logout(req2);

            Assertions.assertNotNull(res2, "Logout not returned");

        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Logout haven't logged in")
    public void logoutFail() {
        try {
            LogoutRequest req = new LogoutRequest("a;lsdkfj;asdlkf");
            facade.logout(req);
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized");
        }
    }

    @Test
    @DisplayName("List Games Success")
    public void listGamesSuccess() {
        try {
            RegisterRequest req = new RegisterRequest("p", "p", "p");
            RegisterResult res = facade.register(req);
            CreateGameRequest req1 = new CreateGameRequest(res.authToken(), "gameName");
            CreateGameResult res1 = facade.createGame(req1);

            ListGamesRequest req2 = new ListGamesRequest(res.authToken());
            ListGamesResult res2 = facade.listGames(req2);
            Assertions.assertEquals(1, res2.games().size(), "Games not listed correctly");

        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("List Games no auth")
    public void listGames() {
        try {
            RegisterRequest req = new RegisterRequest("p", "p", "p");
            RegisterResult res = facade.register(req);
            CreateGameRequest req1 = new CreateGameRequest(res.authToken(), "gameName");
            CreateGameResult res1 = facade.createGame(req1);

            ListGamesRequest req2 = new ListGamesRequest("a;lsdkfj;asldkjf");
            ListGamesResult res2 = facade.listGames(req2);

        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized");
        }
    }

    @Test
    @DisplayName("Delete DB Success")
    public void clearSuccess() {
        try {
            RegisterRequest req = new RegisterRequest("p", "p", "p");
            RegisterResult res = facade.register(req);
            facade.deleteDB();
            ListGamesRequest req2 = new ListGamesRequest(res.authToken());
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized");
        }
    }






}
