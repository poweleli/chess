package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

public class GameSQL implements GameDAOInterface{
    private final Gson gson = new Gson();
    Connection conn;
    String createGameTable = """
            CREATE TABLE IF NOT EXISTS game (
              `id` int NOT NULL AUTO_INCREMENT,
              `white_username` varchar(256),
              `black_username` varchar(256),
              `game_name` varchar(256) NOT NULL,
              `chess_game` text NOT NULL,
              PRIMARY KEY (`id`)
            )
            """;


    public GameSQL () throws DataAccessException {
        try {
            DatabaseManager.createDatabase();
            this.conn = DatabaseManager.getConnection();
            try (var preparedStatement = conn.prepareStatement(createGameTable)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        try (var preparedStatement = conn.prepareStatement("INSERT INTO game (game_name, chess_game) VALUES(?,?)",
                                                                PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, gameName);
            preparedStatement.setString(2, gson.toJson(chessGame));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    } else {
                        throw new DataAccessException("Failed to retrieve the generated ID.");
                    }
                }
            } else {
                throw new DataAccessException("No rows affected, failed to insert the game.");
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }


    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement("SELECT * FROM game WHERE id=?")) {
            preparedStatement.setString(1,String.valueOf(gameId));

            try (var rs = preparedStatement.executeQuery()) {
                rs.next();
                ChessGame chessGame = gson.fromJson(rs.getString("chess_game"), ChessGame.class);
                return new GameData(rs.getInt("id"),
                        rs.getString("white_username"),
                        rs.getString("black_username"),
                        rs.getString("game_name"),
                        chessGame);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void addPlayer(int gameID, String playerColor, String username) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {
        try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE game")) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
}
