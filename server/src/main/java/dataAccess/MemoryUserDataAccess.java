package dataAccess;
import model.*;
import java.lang.String;
import java.util.UUID;


public class MemoryUserDataAccess implements UserDataAccess {
    public String getUser(String username) {

    }
    public String register(UserData user) {
        String authToken = createAuth(user.username());
        return authToken;
    }
}
