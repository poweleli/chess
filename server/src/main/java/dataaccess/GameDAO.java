package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class GameDAO implements GameDAOInterface {
    private static GameDAO instance;

    private final Map<String, UserData> users = new HashMap<>();

    private GameDAO() {
        // Private constructor to prevent instantiation
    }

    public static synchronized GameDAO getInstance() {
        if (instance == null) {
            instance = new GameDAO();
        }
        return instance;
    }

    @Override
    public GameData[] listGames() {
        return new GameData[0];
    }

    @Override
    public void createGame(String gameName) {

    }

    @Override
    public GameData getGame(String gameId) {
        return null;
    }
}
