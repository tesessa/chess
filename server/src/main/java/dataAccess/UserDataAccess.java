package dataAccess;
import model.*;
public interface UserDataAccess {
    void createUser(String username, String password, String email);
    UserData getUser(String username);
    void clear();
}
