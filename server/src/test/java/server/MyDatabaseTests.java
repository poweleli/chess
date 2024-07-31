package server;

import dataaccess.AuthSQL;
import dataaccess.DataAccessException;
import dataaccess.UserDAOInterface;
import dataaccess.UserSQL;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import requests.RegisterRequest;
import responses.ErrorResult;
import responses.RegisterResult;
import responses.ResultInterface;

public class MyDatabaseTests {

    //  Register User Tests
    @Test
    @DisplayName("Testing conn")
    public void connSuccess() throws Exception {
        UserSQL user = new UserSQL();
        user.createUser(new UserData("username", "password", "email@email.com"));
        UserData ud = user.getUser("username");
        Assertions.assertEquals(ud.username(), "username");
        Assertions.assertEquals(ud.password(), "password");
        Assertions.assertEquals(ud.email(), "email@email.com");

        user.clear();
        Assertions.assertThrows(DataAccessException.class, () -> {
            user.getUser("username");
        });

    }

    @Test
    @DisplayName("Testing auth")
    public void testAuth() throws Exception {
        AuthSQL auth = new AuthSQL();
        String authToken = auth.createAuth("password");

//        user.createUser(new UserData("username", "password", "email@email.com"));
//        UserData ud = user.getUser("username");
//        Assertions.assertEquals(ud.username(), "username");
//        Assertions.assertEquals(ud.password(), "password");
//        Assertions.assertEquals(ud.email(), "email@email.com");
//
//        user.clear();
//        Assertions.assertThrows(DataAccessException.class, () -> {
//            user.getUser("username");
//        });

    }
}
