package dataaccess;

import model.UserData;

public interface UserDAOInterface {

    public UserData getUser(String username);
}
