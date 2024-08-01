package dataaccess;

import model.AuthData;
import model.UserData;
import requests.RegisterRequest;

import java.util.*;

public class UserDAO implements UserDAOInterface {
    private static UserDAO instance;
    private Map<String, UserData> users;

    private UserDAO() {
        // Private constructor to prevent instantiation
        this.users = new HashMap<>();
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (users.containsKey(username)) {
            return users.get(username);
        }
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        checkValidRequest(userData);
        // unique usernames
        if (getUser(userData.username()) == null) {
            users.put(userData.username(), userData);
        } else {
            throw new DataAccessException("Error: already taken");}
    }

    @Override
    public void checkUserCreds(String username, String password) throws DataAccessException {
        if (users.get(username) == null ||
            !users.get(username).password().equals(password)) {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        users = new HashMap<>();
    }

    private void checkValidRequest(UserData userData) throws DataAccessException {
        if (!(userData.username() != null &&
                userData.username().length() > 0 &&
                userData.password() != null &&
                userData.password().length() > 0 &&
                userData.email() != null &&
                userData.email().length() > 0)) {
            throw new DataAccessException("Error: bad request");
        }
    }
}
