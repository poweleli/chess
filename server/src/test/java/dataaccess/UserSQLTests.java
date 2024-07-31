package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserSQLTests {
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

        Assertions.assertDoesNotThrow(() -> {
            user.createUser(userData);
        }, "Creating a user should not throw an exception");
    }
}
