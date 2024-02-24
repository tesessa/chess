package service;
import dataAccess.UserDataAccess;
import dataAccess.AuthDataAccess;

public class UserService {
    private final UserDataAccess userMemory;
    private final AuthDataAccess authMemory;
   // private final
    public UserService(UserDataAccess userMemory, AuthDataAccess authMemory) {
        this.userMemory = userMemory;
        this.authMemory = authMemory;
    }
    public String register(String username, String password, String email) {
        if(userMemory.getUser(username) == null) {
            userMemory.createUser(username, password, email);
            return authMemory.createAuth(username);
        }
        return "";
    }
   // public AuthData login(UserData user) {}
    //public void logout(UserData user) {}
}
