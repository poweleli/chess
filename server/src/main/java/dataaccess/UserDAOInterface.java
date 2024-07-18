package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAOInterface {
    public UserData getUser(String username);
    public Integer createUser(UserData userData);
    public Integer createAuth(AuthData authData);
}
