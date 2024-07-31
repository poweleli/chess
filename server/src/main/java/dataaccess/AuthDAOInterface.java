package dataaccess;

import model.AuthData;

public interface AuthDAOInterface {
    public String createAuth(String username) throws DataAccessException;;
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void clear() throws DataAccessException;}
