package dataAccess;

import model.*;

public interface AuthDataAccess {
    AuthData createAuth(String username);
    AuthData getAuth(String auth);

    void deleteAuth(String auth);
    void clear();
}
