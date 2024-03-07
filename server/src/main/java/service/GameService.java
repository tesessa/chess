package service;

import ExceptionClasses.AlreadyTakenException;
import ExceptionClasses.BadRequestException;
import ExceptionClasses.UnauthorizedException;
import Result.CreateGameResult;
import Result.ErrorResult;
import Result.GameInformation;
import chess.ChessGame;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.UserDataAccess;
import model.GameData;

import java.sql.SQLException;
import java.util.HashSet;

public class GameService {
    private final UserDataAccess userMemory;
    private final AuthDataAccess authMemory;
    private final GameDataAccess gameMemory;


    public GameService(GameDataAccess gameMemory, UserDataAccess userMemory, AuthDataAccess authMemory) {
        this.gameMemory = gameMemory;
        this.userMemory = userMemory;
        this.authMemory = authMemory;
    }

    public CreateGameResult createGame(String gameName, String auth) throws UnauthorizedException, BadRequestException, DataAccessException, SQLException {
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

    public ErrorResult joinGame(ChessGame.TeamColor color, int gameID, String auth) throws UnauthorizedException, AlreadyTakenException, BadRequestException, DataAccessException, SQLException {
        if(authMemory.getAuth(auth) == null) {
            throw new UnauthorizedException();
        }
        if(gameMemory.getGame(gameID) == null) {
            throw new BadRequestException();
        } else {
            String username = authMemory.getAuth(auth).username();
            GameData game = gameMemory.getGame(gameID);
            if(color == ChessGame.TeamColor.BLACK && game.blackUsername() != null) {
                throw new AlreadyTakenException();
            }
            if (color == ChessGame.TeamColor.WHITE && game.whiteUsername() != null) {
                throw new AlreadyTakenException();
            }
            gameMemory.updateGame(game, username, color);
        }
        ErrorResult r = new ErrorResult("{}");
        return r;
    }

    public HashSet<GameInformation> listGames(String auth) throws UnauthorizedException, DataAccessException, SQLException{
        HashSet<GameInformation> games;
        if(authMemory.getAuth(auth) == null) {
            throw new UnauthorizedException();
        } else {
            games = gameMemory.listGames();
        }
        return games;
    }

    public ErrorResult clear() throws DataAccessException {
            gameMemory.clear();
            userMemory.clear();
            authMemory.clear();
            ErrorResult clear = new ErrorResult("{}");
            return clear;
    }
}
