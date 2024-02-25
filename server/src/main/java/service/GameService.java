package service;

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

    public ErrorResult clear() throws DataAccessException {
            gameMemory.clear();
            userMemory.clear();
            authMemory.clear();
            ErrorResult clear = new ErrorResult("{}");
            return clear;
    }
}
