package server;

import chess.ChessGame;
import dataaccess.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class MyDatabaseTests {

    //  Register User Tests
    @Test
    @DisplayName("Testing user")
    public void testUser() throws Exception {
        UserSQL user = new UserSQL();
        user.clear();
        user.createUser(new UserData("username", "password", "email@email.com"));
        UserData ud = user.getUser("username");
        Assertions.assertEquals(ud.username(), "username");
        Assertions.assertEquals(ud.password(), "password");
        Assertions.assertEquals(ud.email(), "email@email.com");

        user.clear();
        Assertions.assertThrows(DataAccessException.class, () -> {
            user.getUser("username");
        });

    }

    @Test
    @DisplayName("Testing auth")
    public void testAuth() throws Exception {
        AuthSQL auth = new AuthSQL();
        auth.clear();
        String authToken = auth.createAuth("username");
        String authToken2 = auth.createAuth("username2");
        AuthData authData = auth.getAuth(authToken);
        Assertions.assertEquals(authData.username(), "username");
        Assertions.assertEquals(authData.authToken(), authToken);

        auth.deleteAuth(authToken2);
        Assertions.assertThrows(DataAccessException.class, () -> {
            auth.getAuth("username2");
        });
        auth.clear();
        Assertions.assertThrows(DataAccessException.class, () -> {
            auth.getAuth("username");
        });

    }

    @Test
    @DisplayName("Testing game")
    public void testGame() throws Exception {
        GameSQL game = new GameSQL();
        game.clear();
        int gameId = game.createGame("myGame1");
        int gameId2 = game.createGame("myGame2");

        GameData gameData = game.getGame(gameId);
        Assertions.assertEquals(gameData.gameName(), "myGame1");
        Assertions.assertNull(gameData.whiteUsername());
        Assertions.assertNull(gameData.blackUsername());
        Assertions.assertNotNull(gameData.game());

        game.addPlayer(gameId, "WHITE", "username1");
        GameData gameData2 = game.getGame(gameId);
        Assertions.assertEquals(gameData2.gameName(), "myGame1");
        Assertions.assertEquals(gameData2.whiteUsername(), "username1");

        game.addPlayer(gameId2, "BLACK", "username2");

        // Test the listGames method
        Collection<GameData> gameList = game.listGames();
        Assertions.assertEquals(2, gameList.size());

        boolean foundGame1 = false;
        boolean foundGame2 = false;

        for (GameData gameData1 : gameList) {
            if (gameData1.gameName().equals("myGame1")) {
                Assertions.assertEquals(gameData1.whiteUsername(), "username1");
                foundGame1 = true;
            } else if (gameData1.gameName().equals("myGame2")) {
                Assertions.assertEquals(gameData1.blackUsername(), "username2");
                foundGame2 = true;
            }
        }

        Assertions.assertTrue(foundGame1, "Game myGame1 not found in the list");
        Assertions.assertTrue(foundGame2, "Game myGame2 not found in the list");


        game.clear();
        Assertions.assertThrows(DataAccessException.class, () -> {
            game.getGame(gameId);
        });

        Collection<GameData> emptyGameList = game.listGames();
        Assertions.assertTrue(emptyGameList.isEmpty());

    }
}
