package dataaccess;

import model.*;

import java.util.Collection;

public interface GameDAOInterface {
    public Collection<GameData> listGames() throws DataAccessException;
    public int createGame(String gameName) throws DataAccessException;
    public GameData getGame(int gameId) throws DataAccessException;
    public void addPlayer(int gameID, String playerColor, String username) throws DataAccessException;
    public void clear() throws DataAccessException;
}
