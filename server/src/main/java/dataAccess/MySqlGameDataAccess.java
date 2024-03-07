package dataAccess;
import Result.GameInformation;
import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlGameDataAccess implements GameDataAccess {

    public MySqlGameDataAccess() throws DataAccessException {
        configureDatabase();
    }

    public GameData getGame(int gameID) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, json FROM game WHERE gameID=?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try(var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public int createGame(String gameName) throws DataAccessException {
        var statement = "INSERT INTO game (gameName,json) VALUES (?,?)";
        ChessGame game = new ChessGame();
        var json = new Gson().toJson(game);
        var id = executeUpdate(statement, gameName, json);
        return id;
    }

    public void updateGame(GameData game, String username, ChessGame.TeamColor color) throws DataAccessException {
        var json = new Gson().toJson(game);
        var gameId = game.gameID();
        var statement = "";
        if(color == ChessGame.TeamColor.WHITE) {
            statement = "UPDATE game SET whiteUsername=? WHERE  gameID=?";
        } else if(color == ChessGame.TeamColor.BLACK) {
            statement = "UPDATE game SET blackUsername=? WHERE gameID=?";
        } else {
            return;
        }
        executeUpdate(statement, username, game.gameID());

    }

    public HashSet<GameInformation> listGames() throws DataAccessException {
        var result = new HashSet<GameInformation>();
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName FROM game";
            try(var ps = conn.prepareStatement(statement)) {
                try(var rs = ps.executeQuery()) {
                    while(rs.next()) {
                        result.add(readGameInformation(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("gameID");
        var json = rs.getString("json");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var game = new Gson().fromJson(json, ChessGame.class);
        GameData gameInfo = new GameData(id, whiteUsername, blackUsername, gameName, game);
        return gameInfo;
    }

    private GameInformation readGameInformation(ResultSet rs) throws SQLException {
        var id = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        GameInformation gameInfo = new GameInformation(id, whiteUsername, blackUsername, gameName);
        return gameInfo;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for(var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if(param instanceof String p) ps.setString(i+1, p);
                    else if (param instanceof  Integer p) ps.setInt(i+1, p);
                    else if (param instanceof  ChessGame p) ps.setString(i+1, p.toString());
                    else if (param == null) ps.setNull(i+1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if(rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s %s", statement, e.getMessage()));
        }
    }

    private final String[] createGames = {
            """
            CREATE TABLE IF NOT EXISTS game (
              gameID int NOT NULL AUTO_INCREMENT,
              whiteUsername varchar(256),
              blackUsername varchar(256),
              gameName varchar(256) NOT NULL,
              json TEXT DEFAULT NULL,
              PRIMARY KEY (gameID)
            );
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()) {
            for (var game : createGames) {
                try (var preparedStatement = conn.prepareStatement(game)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch(SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }

    }
}
