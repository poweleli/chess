package dataaccess;

import model.UserData;
import org.junit.jupiter.api.*;

public class UserSQLTests {
    @BeforeAll
    public static void init() throws Exception {
        UserSQL user = new UserSQL();
        user.clear();
    }

    @AfterEach
    public void cleanup() throws Exception {
        UserSQL user = new UserSQL();
        user.clear();
    }

    @Test
    @DisplayName("Create User Success")
    public void createUserSuccess() throws Exception {
        UserSQL user = new UserSQL();
        UserData userData = new UserData("username", "password", "email@email.com");

        Assertions.assertDoesNotThrow(() -> {
            user.createUser(userData);
        }, "Creating a user should not throw an exception");
    }

    @Test
    @DisplayName("Create User 2x")
    public void createUserFailure() throws Exception {
        UserSQL user = new UserSQL();
        UserData userData = new UserData("username", "password", "email@email.com");
        user.createUser(userData);

        Assertions.assertThrows(DataAccessException.class, () -> {
            user.createUser(userData);
        });
    }

    @Test
    @DisplayName("Get User Success")
    public void getUserSuccess() throws Exception {
        UserSQL user = new UserSQL();
        UserData userData = new UserData("username", "password", "email@email.com");
        user.createUser(userData);

        UserData userDatares = user.getUser("username");
        Assertions.assertEquals("username", userDatares.username());
        Assertions.assertEquals("email@email.com", userDatares.email());
    }

    @Test
    @DisplayName("Get User Not Exist")
    public void getUserFailure() throws Exception {
        UserSQL user = new UserSQL();
        Assertions.assertThrows(DataAccessException.class, () -> {
            user.getUser("username");
        });
    }

    @Test
    @DisplayName("Check User Creds Success")
    public void checkUserCredsSuccess() throws Exception {
        UserSQL user = new UserSQL();
        UserData userData = new UserData("username", "password", "email@email.com");
        user.createUser(userData);

        Assertions.assertDoesNotThrow(() -> {
            user.checkUserCreds(userData.username(), userData.password());
        }, "Checking correct user creds should not throw an exception");
    }

    @Test
    @DisplayName("Check User Wrong Password")
    public void checkUserCredsFailure() throws Exception {
        UserSQL user = new UserSQL();
        UserData userData = new UserData("username", "password", "email@email.com");
        user.createUser(userData);

        Assertions.assertThrows(DataAccessException.class, () -> {
            user.checkUserCreds(userData.username(), "pasword");
        });
    }

    @Test
    @DisplayName("Clear Success")
    public void clearSuccess() throws Exception {
        UserSQL user = new UserSQL();
        UserData userData = new UserData("username", "password", "email@email.com");
        user.createUser(userData);
        user.clear();

        Assertions.assertThrows(DataAccessException.class, () -> {
            user.getUser(userData.username());
        });
    }




}
