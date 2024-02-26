package service;

import ExceptionClasses.BadRequestException;
import ExceptionClasses.UnauthorizedException;
import Result.CreateGameResult;
import Result.ErrorResult;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.UserDataAccess;

public class GameService {
    private final UserDataAccess userMemory;
    private final AuthDataAccess authMemory;
    private final GameDataAccess gameMemory;


    public GameService(GameDataAccess gameMemory, UserDataAccess userMemory, AuthDataAccess authMemory) {
        this.gameMemory = gameMemory;
        this.userMemory = userMemory;
        this.authMemory = authMemory;
    }

    public CreateGameResult createGame(String gameName, String auth) throws UnauthorizedException, BadRequestException {
        CreateGameResult gameID;
       if(authMemory.getAuth(auth) == null) {
           throw new UnauthorizedException();
       } else {
           if(gameName == null) {
               throw new BadRequestException();
           }
           gameID = new CreateGameResult(gameMemory.createGame(gameName));
       }
       return gameID;
    }

    public ErrorResult joinGame(String gameName, String auth) throws UnauthorizedException {
        if(authMemory.getAuth(auth) == null) {
            throw new UnauthorizedException();
        }
    }

    public ErrorResult clear() throws DataAccessException {
            gameMemory.clear();
            userMemory.clear();
            authMemory.clear();
            ErrorResult clear = new ErrorResult("{}");
            return clear;
    }
}
