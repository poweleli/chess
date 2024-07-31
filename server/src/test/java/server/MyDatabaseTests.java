package server;

import dataaccess.*;
import model.*;
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
    @DisplayName("Testing user")
    public void testUser() throws Exception {
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
        auth.clear();
        String authToken = auth.createAuth("username");
        String authToken2 = auth.createAuth("username2");
        AuthData authData = auth.getAuth(authToken);
        Assertions.assertEquals(authData.username(), "username");
        Assertions.assertEquals(authData.authToken(), authToken);

        auth.deleteAuth(authToken2);
        Assertions.assertThrows(DataAccessException.class, () -> {
            auth.getAuth("username2");
        });
        auth.clear();
        Assertions.assertThrows(DataAccessException.class, () -> {
            auth.getAuth("username");
        });

    }

    @Test
    @DisplayName("Testing game")
    public void testGame() throws Exception {
        GameSQL game = new GameSQL();
//        auth.clear();
//        String authToken = auth.createAuth("username");
//        String authToken2 = auth.createAuth("username2");
//        AuthData authData = auth.getAuth(authToken);
//        Assertions.assertEquals(authData.username(), "username");
//        Assertions.assertEquals(authData.authToken(), authToken);
//
//        auth.deleteAuth(authToken2);
//        Assertions.assertThrows(DataAccessException.class, () -> {
//            auth.getAuth("username2");
//        });
//        auth.clear();
//        Assertions.assertThrows(DataAccessException.class, () -> {
//            auth.getAuth("username");
//        });

    }
}
