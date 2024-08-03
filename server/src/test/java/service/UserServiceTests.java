package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import requests.*;
import responses.*;



public class UserServiceTests {
    private static final UserService USER_SERVICE;

    static {
        try {
            USER_SERVICE = new UserService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() throws DataAccessException {
        USER_SERVICE.clear();
    }

//  Register User Tests
    @Test
    @DisplayName("Register")
    public void registerUserSuccess(){
        try {
            RegisterRequest req = new RegisterRequest("username", "password", "email");
            RegisterResult res = USER_SERVICE.register(req);

            Assertions.assertEquals(res.username(), req.username(), "Usernames of request and result match");
        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Register Existing Username")
    public void registerUserFailure() {
        try {
            RegisterRequest req = new RegisterRequest("username", "password", "email");
            RegisterResult res = USER_SERVICE.register(req);

            RegisterRequest req1 = new RegisterRequest("username", "password", "email");
            RegisterResult res1 = USER_SERVICE.register(req);
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: already taken", "Throws incorrect error message");
        }
    }

    // Login User Tests
    @Test
    @DisplayName("Login Existing User")
    public void loginUserSuccess() {
        try {
            RegisterRequest req = new RegisterRequest("username", "password", "email");
            RegisterResult res = USER_SERVICE.register(req);

            LoginRequest req1 = new LoginRequest("username", "password");
            LoginResult res1 = USER_SERVICE.login(req1);

            Assertions.assertEquals(res1.username(), "username", "Usernames of request and result so not match");
        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Login Non-Existing User")
    public void loginUserFailure() throws Exception {
        try {
            LoginRequest req1 = new LoginRequest("username", "password");
            LoginResult res1 = USER_SERVICE.login(req1);
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Throws incorrect Error Message");

        }
    }


    // Logout user Tests
    @Test
    @DisplayName("Logout User")
    public void logoutUserSuccess() {
        try {
            RegisterRequest req = new RegisterRequest("username", "password", "email");
            RegisterResult res = USER_SERVICE.register(req);

            LoginRequest req1 = new LoginRequest("username", "password");
            LoginResult res1 = USER_SERVICE.login(req1);

            String authToken = res1.authToken();
            LogoutRequest req2 = new LogoutRequest(authToken);
            LogoutResult res2 = USER_SERVICE.logout(req2);

            Assertions.assertNotNull(res2, "Result is not a Logout Result object.");
        } catch (Exception e) {
            Assertions.assertTrue(Boolean.FALSE, e.getMessage());
        }
    }

    @Test
    @DisplayName("Logout User No AuthToken")
    public void logoutUserFailure() {
        try {
            RegisterRequest req = new RegisterRequest("username", "password", "email");
            RegisterResult res = USER_SERVICE.register(req);

            LoginRequest req1 = new LoginRequest("username", "password");
            LoginResult res1 = USER_SERVICE.login(req1);

            LogoutRequest req2 = new LogoutRequest("");
            LogoutResult res2 = USER_SERVICE.logout(req2);

        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Throws incorrect Error Message");

        }
    }

    // Clear User Tests
    @Test
    @DisplayName("Clear User")
    public void clearUser() {
        try {
            RegisterRequest req = new RegisterRequest("username", "password", "email");
            RegisterResult res = USER_SERVICE.register(req);

            ClearResult res1 = USER_SERVICE.clear();

            LoginRequest req2 = new LoginRequest("username", "password");
            LoginResult res2 = USER_SERVICE.login(req2);
        } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(), "Error: unauthorized", "Able to login even though cleared db.");

        }
    }
}
