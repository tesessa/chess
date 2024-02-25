package dataAccess;

import model.*;

public interface AuthDataAccess {
    AuthData createAuth(String username);

    void clear();
}
