package dataAccess;
import model.*;

import java.sql.SQLException;

public interface UserDataAccess {
    String checkPassword(String password, String username) throws DataAccessException, SQLException;
    void createUser(String username, String password, String email) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException, SQLException;
    void clear() throws DataAccessException;
}
