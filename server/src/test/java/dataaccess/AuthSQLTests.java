package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class AuthSQLTests {
    @BeforeEach
    public void cleanup() throws Exception {
        AuthSQL auth = new AuthSQL();
        auth.clear();
    }

    @Test
    @DisplayName("Create Auth Success")
    public void createAuthSuccess() throws Exception {
        AuthSQL auth = new AuthSQL();
        String authToken = auth.createAuth("username");
        String authToken2 = auth.createAuth("username2");

        Assertions.assertNotEquals(authToken, authToken2);
    }

    @Test
    @DisplayName("Create Auth 2x")
    public void createAuthFailure() throws Exception {
        AuthSQL auth = new AuthSQL();
        String authToken = auth.createAuth("username");
        String authToken2 = auth.createAuth("username");

        Assertions.assertNotEquals(authToken, authToken2);
    }

    @Test
    @DisplayName("Get Auth Success")
    public void getAuthSuccess() throws Exception {
        AuthSQL auth = new AuthSQL();
        String authToken = auth.createAuth("username");
        AuthData authData = auth.getAuth(authToken);

        Assertions.assertEquals("username", authData.username());
        Assertions.assertEquals(authToken, authData.authToken());
    }

    @Test
    @DisplayName("Get Auth Unauthorized")
    public void getAuthFailure() throws Exception {
        AuthSQL auth = new AuthSQL();
        Assertions.assertThrows(DataAccessException.class, () -> {
            auth.getAuth("username");
        });
    }


    @Test
    @DisplayName("Delete Auth Success")
    public void deleteAuthSuccess() throws Exception {
        AuthSQL auth = new AuthSQL();
        String authToken = auth.createAuth("username");
        auth.deleteAuth(authToken);

        Assertions.assertThrows(DataAccessException.class, () -> {
            auth.getAuth(authToken);
        });

    }

    @Test
    @DisplayName("Delete Auth Unauthorized")
    public void deleteAuthFailure() throws Exception {
        AuthSQL auth = new AuthSQL();
        Assertions.assertThrows(DataAccessException.class, () -> {
            auth.deleteAuth("username");
        });
    }

    @Test
    @DisplayName("Clear Success")
    public void clearSuccess() throws Exception {
        AuthSQL auth = new AuthSQL();
        auth.createAuth("username");
        auth.clear();
        Assertions.assertThrows(DataAccessException.class, () -> {
            auth.getAuth("username");
        });
    }

}