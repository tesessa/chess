package dataAccess;

import model.AuthData;

import java.util.UUID;

public class MemoryAuthDataAccess implements AuthDataAccess {
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        return authToken;
    }
}
