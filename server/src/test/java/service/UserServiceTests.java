package service;

import org.junit.jupiter.api.*;
import requests.*;
import responses.*;



public class UserServiceTests {
    private static final UserService USER_SERVICE = UserService.getInstance();

    @BeforeEach
    public void setup() {
        USER_SERVICE.clear();
    }

//  Register User Tests
    @Test
    @DisplayName("Register")
    public void registerUserSuccess() throws Exception {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        ResultInterface res = USER_SERVICE.register(req);

        Assertions.assertTrue(res instanceof RegisterResult, "Result is not a Register Result object.");
        Assertions.assertEquals(((RegisterResult) res).username(), req.username(), "Usernames of request and result match");
    }

    @Test
    @DisplayName("Register Existing Username")
    public void registerUserFailure() throws Exception {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        ResultInterface res = USER_SERVICE.register(req);

        RegisterRequest req1 = new RegisterRequest("username", "password", "email");
        ResultInterface res1 = USER_SERVICE.register(req);

        Assertions.assertTrue(res1 instanceof ErrorResult, "Result does not throw correct error.");
        Assertions.assertEquals(((ErrorResult) res1).message(), "Error: already taken", "Throws correct error message");
    }

    // Login User Tests
    @Test
    @DisplayName("Login Existing User")
    public void loginUserSuccess() throws Exception {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        ResultInterface res = USER_SERVICE.register(req);

        LoginRequest req1 = new LoginRequest("username", "password");
        ResultInterface res1 = USER_SERVICE.login(req1);

        Assertions.assertTrue(res1 instanceof LoginResult, "Result is not a Login Result object.");
        Assertions.assertEquals(((LoginResult) res1).username(), "username", "Usernames of request and result so not match");
    }

    @Test
    @DisplayName("Login Non-Existing User")
    public void loginUserFailure() throws Exception {
        LoginRequest req1 = new LoginRequest("username", "password");
        ResultInterface res1 = USER_SERVICE.login(req1);

        Assertions.assertTrue(res1 instanceof ErrorResult, "Result does not throw correct error.");
        Assertions.assertEquals(((ErrorResult) res1).message(), "Error: unauthorized", "Throws incorrect Error Message");
    }


    // Logout user Tests
    @Test
    @DisplayName("Logout User")
    public void logoutUserSuccess() throws Exception {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        ResultInterface res = USER_SERVICE.register(req);

        LoginRequest req1 = new LoginRequest("username", "password");
        ResultInterface res1 = USER_SERVICE.login(req1);

        String authToken = ((LoginResult) res1).authToken();
        LogoutRequest req2 = new LogoutRequest(authToken);
        ResultInterface res2 = USER_SERVICE.logout(req2);

        Assertions.assertTrue(res2 instanceof LogoutResult, "Result is not a Logout Result object.");
    }

    @Test
    @DisplayName("Logout User No AuthToken")
    public void logoutUserFailure() throws Exception {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        ResultInterface res = USER_SERVICE.register(req);

        LoginRequest req1 = new LoginRequest("username", "password");
        ResultInterface res1 = USER_SERVICE.login(req1);

        LogoutRequest req2 = new LogoutRequest("");
        ResultInterface res2 = USER_SERVICE.logout(req2);

        Assertions.assertTrue(res2 instanceof ErrorResult, "Result is not an Error object.");
        Assertions.assertEquals(((ErrorResult) res2).message(), "Error: unauthorized", "Throws incorrect Error Message");
    }

    // Clear User Tests
    @Test
    @DisplayName("Clear User")
    public void clearUser() throws Exception {
        RegisterRequest req = new RegisterRequest("username", "password", "email");
        ResultInterface res = USER_SERVICE.register(req);

        ResultInterface res1 = USER_SERVICE.clear();

        LoginRequest req2 = new LoginRequest("username", "password");
        ResultInterface res2 = USER_SERVICE.login(req2);

        Assertions.assertTrue(res2 instanceof ErrorResult, "Able to login even though cleared db.");
        Assertions.assertEquals(((ErrorResult) res2).message(), "Error: unauthorized","Able to login even though cleared db.");
    }
}
