package service;

import dataaccess.*;
import model.*;
import requests.*;
import responses.*;

public class UserService {
    private final UserDAOInterface userDao;
    private final AuthDAOInterface authDao;
    private static UserService instance;

    public UserService() throws Exception{
        // Private constructor to prevent instantiation
        this.userDao = new UserSQL();
        this.authDao = new AuthSQL();
    }

    public RegisterResult register(RegisterRequest req) throws DataAccessException{
        UserData userData = new UserData(req.username(), req.password(), req.email());
        userDao.createUser(userData);

        String authToken = authDao.createAuth(req.username());
        return new RegisterResult(req.username(), authToken);
    }

    public LoginResult login(LoginRequest req) throws DataAccessException{
        userDao.checkUserCreds(req.username(), req.password());
        String authToken = authDao.createAuth(req.username());
        return new LoginResult(req.username(), authToken);
    }

    public LogoutResult logout(LogoutRequest req) throws DataAccessException{
        authDao.deleteAuth(req.authToken());
        return new LogoutResult();
    }

    public ClearResult clear() throws DataAccessException {
        userDao.clear();
        authDao.clear();
        return new ClearResult();
    }

}
