package dataaccess;

import model.AuthData;

public interface AuthDAOInterface {
    public void createAuth(AuthData authData);
    public AuthData getAuth(String authToken);
    public void deleteAuth(AuthData authData);
    public void clear() throws DataAccessException;}
