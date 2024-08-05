package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameSQL implements GameDAOInterface{
    private final Gson gson = new Gson();
//    Connection conn;
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
            try (Connection conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement(createGameTable)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException{
        List<GameData> gameList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM game")) {
                try (var rs = preparedStatement.executeQuery()) {
                    while (rs.next()) {
                        ChessGame chessGame = gson.fromJson(rs.getString("chess_game"), ChessGame.class);
                        gameList.add(new GameData(rs.getInt("id"),
                                rs.getString("white_username"),
                                rs.getString("black_username"),
                                rs.getString("game_name"),
                                chessGame));
                    }
                    return gameList;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        if (gameName != null && !gameName.trim().isEmpty()) {
            ChessGame chessGame = new ChessGame();
            try (Connection conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement("INSERT INTO game (game_name, chess_game) VALUES(?,?)",
                        PreparedStatement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, gameName);
                    preparedStatement.setString(2, gson.toJson(chessGame));
                    preparedStatement.executeUpdate();

                    try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                        generatedKeys.next();
                        return generatedKeys.getInt(1);
                    }
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        }
        throw new DataAccessException("Error: bad request");
    }


    @Override
    public GameData getGame(int gameId) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT * FROM game WHERE id=?")) {
                preparedStatement.setString(1, String.valueOf(gameId));

                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        ChessGame chessGame = gson.fromJson(rs.getString("chess_game"), ChessGame.class);
                        return new GameData(rs.getInt("id"),
                                rs.getString("white_username"),
                                rs.getString("black_username"),
                                rs.getString("game_name"),
                                chessGame);
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
    public void addPlayer(int gameID, String playerColor, String username) throws DataAccessException {
        checkValidColor(playerColor);
        GameData chessGame = getGame(gameID);
        if (playerColor.equals("WHITE") && chessGame.whiteUsername() == null) {
            try (Connection conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement("UPDATE game SET white_username=? WHERE id=?")) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, String.valueOf(gameID));
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } else if (playerColor.equals("BLACK") && chessGame.blackUsername() == null) {
            try (Connection conn = DatabaseManager.getConnection()) {
                try (var preparedStatement = conn.prepareStatement("UPDATE game SET black_username=? WHERE id=?")) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, String.valueOf(gameID));
                    preparedStatement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage());
            }
        } else {
            throw new DataAccessException("Error: already taken");
        }
    }

    private void checkValidColor(String playerColor) throws DataAccessException{
        if (!(playerColor != null &&
                (playerColor.equalsIgnoreCase("WHITE") ||
                        playerColor.equalsIgnoreCase("BLACK")))) {
            throw new DataAccessException("Error: bad request");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE game")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }

    }
}
