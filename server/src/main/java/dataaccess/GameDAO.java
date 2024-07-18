package dataaccess;

import model.AuthData;
import model.GameData;

public class GameDAO implements GameDAOInterface {
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
