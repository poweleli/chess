package dataaccess;

import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.EnumSet;

public class GameSQLTests {

    @AfterEach
    public void cleanup() throws Exception {
        GameSQL game = new GameSQL();
        game.clear();
    }

    @Test
    @DisplayName("Create Game Success")
    public void createGameSuccess() throws Exception {
        GameSQL game = new GameSQL();
        int gameId = game.createGame("New_Game");
        Assertions.assertEquals(1, gameId);
    }

    @Test
    @DisplayName("Create Game Empty String")
    public void createGameFailure() throws Exception {
        GameSQL game = new GameSQL();
        Assertions.assertThrows(DataAccessException.class, () -> {
            game.createGame("");
        });
    }

    @Test
    @DisplayName("Get Game Success")
    public void getGameSuccess() throws Exception {
        GameSQL game = new GameSQL();
        int gameId = game.createGame("New_Game");
        GameData gotGame = game.getGame(gameId);

        Assertions.assertEquals(gotGame.gameName(), "New_Game");
        Assertions.assertNull(gotGame.whiteUsername());
        Assertions.assertNull(gotGame.blackUsername());
        Assertions.assertNotNull(gotGame.game());
    }

    @Test
    @DisplayName("Get Game No Id Match")
    public void getGameFailure() throws Exception {
        GameSQL game = new GameSQL();
        Assertions.assertThrows(DataAccessException.class, () -> {
            game.getGame(0);
        });
    }

    @Test
    @DisplayName("Add Player Success")
    public void addPlayerSuccess() throws Exception {
        GameSQL game = new GameSQL();
        int gameId = game.createGame("New_Game");
        game.addPlayer(gameId, "WHITE", "username");

        GameData gotGame = game.getGame(gameId);

        Assertions.assertEquals("New_Game", gotGame.gameName());
        Assertions.assertEquals("username", gotGame.whiteUsername());
        Assertions.assertNull(gotGame.blackUsername());
        Assertions.assertNotNull(gotGame.game());
    }

    @Test
    @DisplayName("Add Player Invalid Color")
    public void addPlayerFailure() throws Exception {
        GameSQL game = new GameSQL();
        int gameId = game.createGame("New_Game");

        Assertions.assertThrows(DataAccessException.class, () -> {
            game.addPlayer(gameId, "PURPLE", "username");;
        });
    }

    @Test
    @DisplayName("Add Player Already Taken")
    public void addPlayerFailure2() throws Exception {
        GameSQL game = new GameSQL();
        int gameId = game.createGame("New_Game");
        game.addPlayer(gameId, "WHITE", "username_test");

        Assertions.assertThrows(DataAccessException.class, () -> {
            game.addPlayer(gameId, "WHITE", "username");;
        });
    }

    @Test
    @DisplayName("List Games Success")
    public void listGamesSuccess() throws Exception {
        GameSQL game = new GameSQL();
        int gameId = game.createGame("myGame1");
        int gameId2 = game.createGame("myGame2");

        Collection<GameData> gameList = game.listGames();
        Assertions.assertEquals(2, gameList.size());

        boolean foundGame1 = false;
        boolean foundGame2 = false;

        for (GameData gameData1 : gameList) {
            if (gameData1.gameName().equals("myGame1")) {
                Assertions.assertNull(gameData1.whiteUsername());
                foundGame1 = true;
            } else if (gameData1.gameName().equals("myGame2")) {
                Assertions.assertNull(gameData1.blackUsername());
                foundGame2 = true;
            }
        }

        Assertions.assertTrue(foundGame1, "Game myGame1 not found in the list");
        Assertions.assertTrue(foundGame2, "Game myGame2 not found in the list");

    }

    @Test
    @DisplayName("List Games No Games")
    public void listGamesFailure() throws Exception {
        GameSQL game = new GameSQL();
        Collection<GameData> gameList = game.listGames();
        Assertions.assertEquals(0, gameList.size());
    }

}
