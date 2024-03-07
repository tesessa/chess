package service;
import ExceptionClasses.AlreadyTakenException;
import ExceptionClasses.BadRequestException;
import Result.ErrorResult;
import Result.RegisterResult;
import dataAccess.*;
import dataAccess.UserDataAccess;
import dataAccess.AuthDataAccess;
import model.*;
import ExceptionClasses.*;

import java.sql.SQLException;

public class UserService {
    private final UserDataAccess userMemory;
    private final AuthDataAccess authMemory;
   // private final
    public UserService(UserDataAccess userMemory, AuthDataAccess authMemory) {
        this.userMemory = userMemory;
        this.authMemory = authMemory;
    }
    public RegisterResult register(String username, String password, String email) throws AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
            if(username == null || password == null || email == null) {
                throw new BadRequestException();
            }
            if (userMemory.getUser(username) == null) {
                userMemory.createUser(username, password, email);
                AuthData auth = authMemory.createAuth(username);
                RegisterResult r = new RegisterResult(username, auth.authToken());
                return r;
            } else {
                throw new AlreadyTakenException();
            }
    }

    public RegisterResult login(String username, String password) throws UnauthorizedException, DataAccessException, SQLException {
        if(userMemory.getUser(username) == null || userMemory.checkPassword(password) == null ||  !(userMemory.checkPassword(password).equals(username))) {
            throw new UnauthorizedException();
        } else {
            AuthData auth = authMemory.createAuth(username);
            RegisterResult r = new RegisterResult(username, auth.authToken());
            return r;
        }
    }

    public ErrorResult logout(String auth) throws UnauthorizedException, DataAccessException, SQLException {
       if(authMemory.getAuth(auth) == null) {
           throw new UnauthorizedException();
       } else {
           authMemory.deleteAuth(auth);
           ErrorResult r = new ErrorResult("");
           return r;
       }
    }

}
