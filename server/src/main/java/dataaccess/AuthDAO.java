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
        auths.put(authToken, authData);
        return authToken;

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData authData = auths.get(authToken);
        if (authData != null) {
            return authData;
        } else {
            throw new DataAccessException("Error: unauthorized" );
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException{
        AuthData authData = auths.get(authToken);
        if (authData != null) {
            auths.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }

    }

    @Override
    public void clear() throws DataAccessException {
        auths = new HashMap<>();
    }
}
