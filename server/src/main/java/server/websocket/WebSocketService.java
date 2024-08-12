package server.websocket;

import dataaccess.*;
import model.GameData;
import service.GameService;
import service.UserService;

public class WebSocketService {

    public WebSocketService wss;
//    public UserService userService;
//    public GameService gameService;
    public GameDAOInterface gameDao;
    public AuthDAOInterface authDao;

    public WebSocketService() {
        try {
//            userService = new UserService();
//            gameService = new GameService();
            gameDao = new GameSQL();
            authDao = new AuthSQL();
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return gameDao.getGame(gameID);
    }
}
