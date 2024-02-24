package dataAccess;

import model.AuthData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDataAccess implements AuthDataAccess {

    HashMap<String, String> ad = new HashMap<String, String>();
    HashSet<String> at = new HashSet<String>();

    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        ad.put(authToken, username);
        at.add(authToken);
        return authToken;
    }
}
