package dataaccess;

import model.AuthData;
import model.UserData;
import requests.RegisterRequest;

import java.util.*;

public class UserDAO implements UserDAOInterface {
    private static UserDAO instance;

    private final Map<String, UserData> users = new HashMap<>();

    private UserDAO() {
        // Private constructor to prevent instantiation
    }

    public static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
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
