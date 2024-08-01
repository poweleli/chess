package dataaccess;

import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Objects;

public class UserSQL implements UserDAOInterface{
//    Connection conn;
    String createUserTable = """
            CREATE TABLE IF NOT EXISTS user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            )
            """;


    public UserSQL () throws DataAccessException {
        try {DatabaseManager.createDatabase();
            try (Connection conn = DatabaseManager.getConnection();) {
                try (var preparedStatement = conn.prepareStatement(createUserTable)) {
                    preparedStatement.executeUpdate();
                }
        }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE username=?")) {
                preparedStatement.setString(1, username);

                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return new UserData(rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("email"));
                    } else {
                        throw new DataAccessException("Error: bad request");
                    }
                }
        }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public void createUser(UserData userData) throws DataAccessException {
        Boolean userExists;
        try {
            getUser(userData.username());
            userExists = Boolean.TRUE;
        } catch (DataAccessException m) {
            userExists = Boolean.FALSE;
        }
        if (!userExists) {
            checkValidRequest(userData);
            storeUserPassword(userData);
        } else {
            throw new DataAccessException("Error: already taken");
        }
    }

    private void storeUserPassword(UserData userData) throws DataAccessException {
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        writeHashedPasswordToDatabase(userData, hashedPassword);
    }

    private void writeHashedPasswordToDatabase(UserData userData, String hashedPassword) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, userData.email());

                preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private void checkValidRequest(UserData userData) throws DataAccessException {
        if (userData.username() == null ||
            userData.password() == null ||
            userData.email() == null ||
            !(userData.username().matches("^(?=.*[a-zA-Z0-9])[a-zA-Z0-9_-]+$") &&
            userData.password().matches("^(?=.*[a-zA-Z0-9])[a-zA-Z0-9_-]+$")
//                &&
//            userData.email().matches("^(?=.*[a-zA-Z0-9])[a-zA-Z0-9_-]+$"))
        )) {
            throw new DataAccessException("Error: bad request");
        }
    }


    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE user")) {
                preparedStatement.executeUpdate();
        }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void checkUserCreds(String username, String password) throws DataAccessException {
        try {
            UserData userData = getUser(username);
            if (!BCrypt.checkpw(password, userData.password())) {
                throw new DataAccessException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
