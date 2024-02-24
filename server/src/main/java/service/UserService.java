package service;
import model.*;
import dataAccess.UserDataAccess;
import dataAccess.AuthDataAccess;
import dataAccess.GameDataAccess;
import java.util.UUID;

public class UserService {
    private final UserDataAccess userData;
   // private final
    public UserService(UserDataAccess userData) {
        this.userData = userData;
    }
    public AuthData register(UserData user) {
     //   if(getUser(user.username()) == null)
        return userData.register(user);
    }
   // public AuthData login(UserData user) {}
    //public void logout(UserData user) {}
}
