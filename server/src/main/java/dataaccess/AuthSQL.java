package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class AuthSQL implements AuthDAOInterface{
//    Connection conn;
    String createAuthTable = """
        CREATE TABLE IF NOT EXISTS auth (
                  `username` varchar(256) NOT NULL,
                  `authtoken` varchar(256) NOT NULL,
                  PRIMARY KEY (`username`, `authtoken`)
                );
            """;


    public AuthSQL () throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
            try (Connection conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement(createAuthTable)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var insertStatement = conn.prepareStatement("INSERT INTO auth (username, authtoken) VALUES(?, ?)")) {
                insertStatement.setString(1, username);
                insertStatement.setString(2, authToken);
                insertStatement.executeUpdate();
            }
            return authToken;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE authtoken=?")) {
                preparedStatement.setString(1, authToken);

                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        return new AuthData(rs.getString("username"),
                                rs.getString("authtoken"));
                    } else {
                        throw new DataAccessException("Error: unauthorized");
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        getAuth(authToken);
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auth WHERE authtoken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}