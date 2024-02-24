package dataAccess;
import model.*;
import java.lang.String;
import java.util.HashMap;
import java.util.UUID;


public class MemoryUserDataAccess implements UserDataAccess {

    HashMap<String, UserData> ud = new HashMap<String, UserData>();


    public UserData getUser(String username) {
        return ud.get(username);
    }
    public void createUser(String username, String password, String email) {
       // String authToken = createAuth(username);
        ud.put(username,new UserData(username,password, email));

    }
}
