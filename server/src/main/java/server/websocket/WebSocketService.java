package server.websocket;

import dataaccess.*;
import model.*;

public class WebSocketService {
    private final UserDAOInterface userDao;
    private final AuthDAOInterface authDao;
    private final GameDAOInterface gameDao;

    public WebSocketService() throws Exception {
        this.userDao = new UserSQL();
        this.authDao = new AuthSQL();
        this.gameDao = new GameSQL();
    }

    public GameData getGameData(int gameID) throws DataAccessException {
        return gameDao.getGame(gameID);
    }

    public AuthData getUser(String authToken) throws DataAccessException {
        return authDao.getAuth(authToken);
    }

}
