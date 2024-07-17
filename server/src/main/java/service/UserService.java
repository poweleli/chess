package service;

import dataaccess.*;
import model.*;

public class UserService {
    public AuthData register(UserData user) {
        UserDAOInterface userDao = new UserDAO();
        return null;}
    public AuthData login(UserData user) {return null;}
    public void logout(UserData user) {}
}
