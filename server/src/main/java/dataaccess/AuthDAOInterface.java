package dataaccess;

import model.AuthData;

public interface AuthDAOInterface {
    public String createAuth(String username);
    public AuthData getAuth(String authToken);
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;}
