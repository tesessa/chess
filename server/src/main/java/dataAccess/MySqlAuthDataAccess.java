package dataAccess;
import model.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlAuthDataAccess implements AuthDataAccess {
    SQL s;
    public MySqlAuthDataAccess() throws DataAccessException {
        configureDatabase();
        s = new SQL();
    }
    public AuthData createAuth(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        var statement = "INSERT INTO auth (authToken, username) VALUES(?, ?)";
        s.executeUpdate(statement, authToken, username);
        AuthData temp = new AuthData(authToken, username);
        return temp;
    }

    public AuthData getAuth(String auth) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth);
                try(var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch(DataAccessException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void deleteAuth(String auth) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        s.executeUpdate(statement, auth);
    }

    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        s.executeUpdate(statement);
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username = rs.getString("username");
        AuthData auth = new AuthData(authToken, username);
        return auth;
    }
   
    private final String[] createAuths = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              authToken varchar(256) NOT NULL,
              username varchar(256) NOT NULL,
              PRIMARY KEY (authToken)
            );
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()) {
            for (var auth : createAuths) {
                try (var preparedStatement = conn.prepareStatement(auth)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch(SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }

    }


}
