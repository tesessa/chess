package dataAccess;

import model.*;

import java.sql.SQLException;

public interface AuthDataAccess {
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String auth) throws DataAccessException, SQLException;

    void deleteAuth(String auth) throws DataAccessException;
    void clear() throws DataAccessException;
}
