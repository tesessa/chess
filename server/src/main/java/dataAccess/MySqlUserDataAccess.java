package dataAccess;
import model.*;
import com.google.gson.Gson;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlUserDataAccess implements UserDataAccess {

    private SQL s;
    public MySqlUserDataAccess() throws DataAccessException {
        s = new SQL();
        s.configureDatabase(createUsers);
    }

    public void createUser(String username, String password, String email) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email) VALUES (?,?,?)";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);
        s.executeUpdate(statement, username, hashedPassword, email);
    }

    public UserData getUser(String username) throws DataAccessException, SQLException {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try(var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try(var rs = ps.executeQuery()) {
                    if(rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch(DataAccessException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public String checkPassword(String password, String username) throws DataAccessException, SQLException {
        if(verifyUser(username, password) == true) {
            return getUser(username).username();
        } else {
            return null;
        }
    }

    public boolean verifyUser(String username, String password) throws DataAccessException, SQLException {
        UserData data = getUser(username);
        if(data == null) {
            return false;
        }
        var hashedPassword = data.password();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, hashedPassword);
    }

    public void clear() throws DataAccessException {
       var statement = "TRUNCATE user";
       s.executeUpdate(statement);
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var username = rs.getString("username");
        var password = rs.getString("password");
        var email = rs.getString("email");
        UserData user = new UserData(username, password, email);
        return user;
    }

    private final String[] createUsers = {
            """
            CREATE TABLE IF NOT EXISTS user (
              username varchar(255) NOT NULL,
              password varchar(256) NOT NULL,
              email varchar(256) NOT NULL,
              PRIMARY KEY (username)
            );
            """
    };
}
