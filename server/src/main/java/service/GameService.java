package service;

import Result.ClearResult;
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

    public ClearResult clear() throws DataAccessException {
            gameMemory.clear();
            userMemory.clear();
            authMemory.clear();
            ClearResult clear = new ClearResult("{}");
            return clear;
    }
}
