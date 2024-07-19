package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAOInterface {
    public UserData getUser(String username) throws DataAccessException;
    public void createUser(UserData userData) throws DataAccessException;
}