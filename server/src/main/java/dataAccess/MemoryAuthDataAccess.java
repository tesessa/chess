package dataAccess;

import model.AuthData;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDataAccess implements AuthDataAccess {

    HashMap<String, String> aData = new HashMap<String, String>();
    HashSet<String> aTokens = new HashSet<String>();

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData auth = new AuthData(authToken, username);
        aData.put(authToken, username);
        aTokens.add(authToken);
         return auth;
    }

    public AuthData getAuth(String auth) {
       if(aData.get(auth) == null) {
           return null;
       }
       String user = aData.get(auth);
       AuthData tempAuth = new AuthData(auth, user);
       return tempAuth;
    }

    public void deleteAuth(String auth) {
        AuthData delete = getAuth(auth);
        aData.remove(auth);
        for(String loop : aTokens) {
            if(loop.equals(delete)) {
                aTokens.remove(loop);
            }
        }

    }

    public void clear() {
        aData.clear();
        aTokens.clear();
    }
}
