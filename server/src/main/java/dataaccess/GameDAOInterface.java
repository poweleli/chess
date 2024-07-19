package dataaccess;

import model.*;

public interface GameDAOInterface {
    public GameData[] listGames();
    public void createGame(String gameName);
    public GameData getGame(String gameId);
    public void clear() throws DataAccessException;
}
