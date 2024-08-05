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

    public ListGamesResult listGame(ListGamesRequest req) throws DataAccessException {
        authDao.getAuth(req.authToken());
        System.out.println("got auth");
        Collection<GameData> games = gameDao.listGames();
        System.out.println("got games");
        return new ListGamesResult(games);
    }

    public CreateGameResult createGame(CreateGameRequest req) throws DataAccessException{
        authDao.getAuth(req.authToken());
        int gameID = gameDao.createGame(req.gameName());
        return new CreateGameResult(gameID);
    }

    public JoinGameResult joinGame(JoinGameRequest req) throws DataAccessException {
        authDao.getAuth(req.authToken());
        AuthData authData = authDao.getAuth(req.authToken());
        gameDao.addPlayer(req.gameID(), req.playerColor(), authData.username());
        return new JoinGameResult();
    }

    public ClearResult clear() throws DataAccessException{
        gameDao.clear();
        return new ClearResult();

    }
}
