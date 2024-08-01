package service;

import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.ListGamesRequest;
import responses.*;

import java.util.Collection;

public class GameService {
    private final GameDAOInterface gameDao;
    private final AuthDAOInterface authDao;
    private static GameService instance;

    public GameService() throws DataAccessException{
        // Private constructor to prevent instantiation
        this.gameDao = new GameSQL();
        this.authDao = new AuthSQL();
    }

    public ResultInterface listGame(ListGamesRequest req) {
        try {
            authDao.getAuth(req.authToken());
            Collection<GameData> games = gameDao.listGames();
            return new ListGamesResult(games);
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }
    public ResultInterface createGame(CreateGameRequest req) {
        try {
            authDao.getAuth(req.authToken());
            int gameID = gameDao.createGame(req.gameName());
            return new CreateGameResult(gameID);
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }

    }

    public ResultInterface joinGame(JoinGameRequest req) {
        try {
            authDao.getAuth(req.authToken());
            AuthData authData = authDao.getAuth(req.authToken());
            gameDao.addPlayer(req.gameID(), req.playerColor(), authData.username());
            return new JoinGameResult();
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }

    public ResultInterface clear(){
        try {
            gameDao.clear();
            return new ClearResult();
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }
}
