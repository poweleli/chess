package service;

import dataaccess.*;
import model.*;
import requests.*;
import responses.*;

import java.util.UUID;

public class UserService {
    UserDAOInterface userDao = new UserDAO();

    public RegisterResult register(RegisterRequest req) {
        if (userDao.getUser(req.username()) == null) {
            UserData userData = new UserData(req.username(), req.password(), req.email());
            userDao.createUser(userData);

            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(req.username(), authToken);
            userDao.createAuth(authData);

            return new RegisterResult(req.username(), authToken);
        }

        return null;
    }

    public LoginResult login(RegisterRequest req) {return null;}
    public void logout(UserData user) {}

}
