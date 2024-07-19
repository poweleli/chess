package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import responses.ClearResult;
import responses.ErrorResult;
import responses.ResultInterface;

public class GameService {
    private final GameDAOInterface gameDao;
    private static GameService instance;

    private GameService() {
        // Private constructor to prevent instantiation
        this.gameDao = GameDAO.getInstance();
    }

    public static synchronized GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public AuthData listGame(UserData user) {return null;}
    public AuthData createGame(UserData user) {return null;}
    public void joinGame(UserData user) {}

    public ResultInterface clear(){
        try {
            gameDao.clear();
            return new ClearResult();
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }
}
