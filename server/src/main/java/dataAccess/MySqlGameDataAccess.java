package dataAccess;
import Result.GameInformation;
import chess.ChessGame;
import model.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlGameDataAccess implements GameDataAccess {

    public GameData getGame(int gameID) {

    }

    public int createGame(String gameName) {
        
    }

    public void updateGame(GameData game, String username, ChessGame.TeamColor color) {

    }

    public HashSet<GameInformation> listGames() {

    }

    public void clear() {

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
              PRIMARY KEY (id),
              INDEX (name)
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
