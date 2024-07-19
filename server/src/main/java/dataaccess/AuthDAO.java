package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthDAO implements AuthDAOInterface{
    private static AuthDAO instance;

    private Map<String, AuthData> auths;

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
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(username, authToken);
        auths.put(username, authData);
        return authToken;

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
