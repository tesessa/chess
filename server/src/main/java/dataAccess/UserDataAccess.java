package dataAccess;
import model.*;
public interface UserDataAccess {
    String register(UserData user);
    String getUser(String username);
}
