package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO implements AuthDAOInterface{
    private static AuthDAO instance;

    private Map<String, UserData> auths;

    private AuthDAO() {
        // Private constructor to prevent instantiation
        this.auths = new HashMap<>();
    }

    public static synchronized AuthDAO getInstance() {
        if (instance == null) {
            instance = new AuthDAO();
        }
        return instance;
    }

    @Override
    public void createAuth(AuthData authData) {

    }

    @Override
    public AuthData getAuth(String authToken) {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) {

    }

    @Override
    public void clear() throws DataAccessException {
        auths = new HashMap<>();
    }
}
