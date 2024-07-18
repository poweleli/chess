package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAOInterface {
    public UserData getUser(String username) throws DataAccessException;
    public Integer createUser(UserData userData) throws DataAccessException;
}
