package dataaccess;

import model.GameData;

import java.util.Collection;

public class GameSQL implements GameDAOInterface{


    public GameSQL() {

    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        return null;
    }

    @Override
    public void addPlayer(int gameID, String playerColor, String username) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
