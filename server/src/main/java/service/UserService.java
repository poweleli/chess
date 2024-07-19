package service;

import dataaccess.*;
import model.*;
import requests.*;
import responses.*;

import javax.xml.transform.Result;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {
    private final UserDAOInterface userDao;
    private final AuthDAOInterface authDao;
    private static UserService instance;

    private UserService() {
        // Private constructor to prevent instantiation
        this.userDao = UserDAO.getInstance();
        this.authDao = AuthDAO.getInstance();
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
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

    public void logout(UserData user) {}

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
