package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GameDAO implements GameDAOInterface {
    private static GameDAO instance;
    private Map<Integer, GameData> games;
    private int gameID = 0;

    private GameDAO() {
        // Private constructor to prevent instantiation
        this.games = new HashMap<>();
    }

    public static synchronized GameDAO getInstance() {
        if (instance == null) {
            instance = new GameDAO();
        }
        return instance;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {

        if (gameName.length() == 0) {
            throw new DataAccessException("Error: bad request");
        }
        gameID = getGameID();
        GameData newGame = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, newGame);
        return gameID;

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        if (games.get(gameID) == null) {
            throw new DataAccessException("Error: bad request");
        } else {
            return games.get(gameID);
        }
    }

    @Override
    public void addPlayer(int gameID, String playerColor, String username) throws DataAccessException {
        checkValidColor(playerColor);
        GameData gameData = getGame(gameID);
        if (playerColor.equals("WHITE")) {
            if (gameData.whiteUsername() == null) {
                games.put(gameID,
                          new GameData(gameID, username, gameData.blackUsername(),gameData.gameName(), gameData.game()));
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } else {
            if (gameData.blackUsername() == null) {
                games.put(gameID,
                        new GameData(gameID, gameData.whiteUsername(), username,gameData.gameName(), gameData.game()));
            } else {
                throw new DataAccessException("Error: already taken");
            }
        }
    }

    public void checkValidColor(String playerColor) throws DataAccessException{
        if (!(playerColor != null &&
            (playerColor.equals("WHITE") ||
             playerColor.equals("BLACK")))) {
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        games = new HashMap<>();
    }

    public int getGameID() {
        gameID = gameID + 1;
        return gameID;
    }
}
