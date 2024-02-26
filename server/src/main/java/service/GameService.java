package service;

import ExceptionClasses.BadRequestException;
import ExceptionClasses.UnauthorizedException;
import Request.JoinGameRequest;
import Result.CreateGameResult;
import Result.ErrorResult;
import chess.ChessGame;
import dataAccess.AuthDataAccess;
import dataAccess.DataAccessException;
import dataAccess.GameDataAccess;
import dataAccess.UserDataAccess;
import model.GameData;

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

    public ErrorResult joinGame(ChessGame.TeamColor color, int gameID, String auth) throws UnauthorizedException, BadRequestException {
        if(authMemory.getAuth(auth) == null) {
            throw new UnauthorizedException();
        }
        if(gameMemory.getGame(gameID) == null) {
            throw new BadRequestException();
        } else {
            String username = authMemory.getAuth(auth).username();
            GameData game = gameMemory.getGame(gameID);
            if(color == ChessGame.TeamColor.BLACK && game.blackUsername() != null) {
                throw new BadRequestException();
            }
            if (color == ChessGame.TeamColor.WHITE && game.whiteUsername() != null) {
                throw new BadRequestException();
            }
            gameMemory.updateGame(game, username, color);
        }
        ErrorResult r = new ErrorResult("{}");
        return r;
    }

    public ErrorResult clear() throws DataAccessException {
            gameMemory.clear();
            userMemory.clear();
            authMemory.clear();
            ErrorResult clear = new ErrorResult("{}");
            return clear;
    }
}
