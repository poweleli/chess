package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class AuthSQL implements AuthDAOInterface{
    Connection conn;
    String createAuthTable = """
            CREATE TABLE IF NOT EXISTS auth (
              `username` varchar(256) NOT NULL,
              `authtoken` varchar(256) NOT NULL,
              PRIMARY KEY (`username`)
            );
            """;


    public AuthSQL () throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
            this.conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(createAuthTable)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public String createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO auth (username, authtoken) VALUES(?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, authToken);

            preparedStatement.executeUpdate();
            return authToken;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement("SELECT * FROM auth WHERE authtoken=?")) {
            preparedStatement.setString(1,authToken);

            try (var rs = preparedStatement.executeQuery()) {
                rs.next();
                return new AuthData(rs.getString("username"),
                        rs.getString("authtoken"));
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auth")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
