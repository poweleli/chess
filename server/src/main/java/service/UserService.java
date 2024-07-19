package service;

import dataaccess.*;
import model.*;
import requests.*;
import responses.*;

import java.util.UUID;

public class UserService {
    private final UserDAOInterface userDao;
    private final AuthDAOInterface authDao;


    public UserService() {
        this.userDao = UserDAO.getInstance();
        this.authDao = AuthDAO.getInstance();
    }


    public ResultInterface register(RegisterRequest req) {
        try {
            UserData userData = new UserData(req.username(), req.password(), req.email());
            userDao.createUser(userData);

            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(req.username(), authToken);
            authDao.createAuth(authData);
            return new RegisterResult(req.username(), authToken);

        } catch (DataAccessException e) {
            return new ErrorResult(e.getMessage());
        }
    }


    public LoginResult login(RegisterRequest req) {return null;}
    public void logout(UserData user) {}

}
