package service;

import dataaccess.*;
import model.*;
import requests.*;
import responses.*;

import java.util.UUID;

public class UserService {
    UserDAOInterface userDao = new UserDAO();

    public ResultInterface register(RegisterRequest req) {
        Boolean validRequest = checkValidRequest(req);
        if (!validRequest) {
            return new ErrorResult(400, "Error: bad request");
        }

        try {
            UserData userData = new UserData(req.username(), req.password(), req.email());
            userDao.createUser(userData);

            String authToken = UUID.randomUUID().toString();
            AuthData authData = new AuthData(req.username(), authToken);
            userDao.createAuth(authData);
            return new RegisterResult(200, req.username(), authToken);

        } catch (DataAccessException e) {
            return new ErrorResult(403, "Error: already taken");
        }
    }


    public LoginResult login(RegisterRequest req) {return null;}
    public void logout(UserData user) {}


    private Boolean checkValidRequest(RegisterRequest req) {
        if (req.username() != null &&
                req.username().length() > 0 &&
                req.password() != null &&
                req.password().length() > 0 &&
                req.email() != null &&
                req.email().length() > 0) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
