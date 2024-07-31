package dataaccess;

import model.UserData;
import java.sql.*;
import java.util.Objects;

public class UserSQL implements UserDAOInterface{
    Connection conn;
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
        try {
            DatabaseManager.createDatabase();
            this.conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(createUserTable)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement("SELECT * FROM user WHERE username=?")) {
            preparedStatement.setString(1,username);

            try (var rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return new UserData(rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"));
                } else {
                    throw new DataAccessException("Error: bad request");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try {
            getUser("username");
            throw new DataAccessException("Error: already taken");
        } catch(DataAccessException m) {
            checkValidRequest(userData);
            try (var preparedStatement = conn.prepareStatement("INSERT INTO user (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
    }

    private void checkValidRequest(UserData userData) throws DataAccessException {
        if (!(userData.username().matches("^(?=.*[a-zA-Z0-9])[a-zA-Z0-9_-]+$") &&
            userData.password().matches("^(?=.*[a-zA-Z0-9])[a-zA-Z0-9_-]+$")
//                &&
//            userData.email().matches("^(?=.*[a-zA-Z0-9])[a-zA-Z0-9_-]+$"))
        )) {
            throw new DataAccessException("Error: bad request");
        }
    }


    @Override
    public void clear() throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE user")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void checkUserCreds(String username, String password) throws DataAccessException {
        UserData userData = getUser(username);
        if (!Objects.equals(userData.password(), password)) {
            throw new DataAccessException("Error: unauthorized");
        }
    }
}
