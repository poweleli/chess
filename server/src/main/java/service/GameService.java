package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.GameDAOInterface;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class GameService {
    private final GameDAOInterface gameDao;

    public GameService() {
        this.gameDao = GameDAO.getInstance();
    }

    public AuthData listGame(UserData user) {return null;}
    public AuthData createGame(UserData user) {return null;}
    public void joinGame(UserData user) {}
}
