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

    public ResultInterface register(RegisterRequest req) {
        try {
            UserData userData = new UserData(req.username(), req.password(), req.email());
            userDao.createUser(userData);

            String authToken = authDao.createAuth(req.username());
            return new RegisterResult(req.username(), authToken);

        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }

    public ResultInterface login(LoginRequest req) {
        try {
            userDao.checkUserCreds(req.username(), req.password());
            String authToken = authDao.createAuth(req.username());
            return new LoginResult(req.username(), authToken);

        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }

    public ResultInterface logout(LogoutRequest req) {
        try {
            authDao.deleteAuth(req.authToken());
            return new LogoutResult();
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }

    public ResultInterface clear() {
        try {
            userDao.clear();
            authDao.clear();
            return new ClearResult();
        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }

}
