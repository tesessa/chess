package service;
import Result.RegisterResult;
import dataAccess.DataAccessException;
import dataAccess.UserDataAccess;
import dataAccess.AuthDataAccess;
import model.*;

public class UserService {
    private final UserDataAccess userMemory;
    private final AuthDataAccess authMemory;
   // private final
    public UserService(UserDataAccess userMemory, AuthDataAccess authMemory) {
        this.userMemory = userMemory;
        this.authMemory = authMemory;
    }
    public RegisterResult register(String username, String password, String email) throws DataAccessException {
       // try {
            if (userMemory.getUser(username) == null) {
                userMemory.createUser(username, password, email);
                AuthData auth = authMemory.createAuth(username);
                RegisterResult r = new RegisterResult(username, auth.authToken(), null);
                return r;
            } else {
               // throw new DataAccessException(403, "Error: already taken");
                RegisterResult r = new RegisterResult(null, null, "Error: already taken");
                return r;
            }
       // } catch (DataAccessException e) {
         //   throw new DataAccessException(400, "Error: bad request");
        //}

    }



   // public AuthData login(UserData user) {}
    //public void logout(UserData user) {}
}
