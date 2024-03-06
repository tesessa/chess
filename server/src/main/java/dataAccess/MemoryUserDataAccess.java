package dataAccess;
import model.*;
import java.lang.String;
import java.util.HashMap;
import java.util.UUID;


public class MemoryUserDataAccess implements UserDataAccess {

    HashMap<String, UserData> ud = new HashMap<String, UserData>();
    HashMap<String, String> getPassword = new HashMap<String, String>();

    public String checkPassword(String password) { return getPassword.get(password); }

    public UserData getUser(String username) { return ud.get(username); }
    public void createUser(String username, String password, String email) throws DataAccessException {
        ud.put(username,new UserData(username,password, email));
        getPassword.put(password, username);

    }

    public void clear() {
        ud.clear();
        getPassword.clear();
    }
}
