package dataAccess;

import Result.GameInformation;
import chess.ChessGame;
import model.GameData;

import java.util.HashSet;

public interface GameDataAccess {
    void clear() throws DataAccessException;

    GameData getGame(int gameID) throws DataAccessException;

    int createGame(String gameName) throws DataAccessException;

    void updateGame(GameData game, String username, ChessGame.TeamColor color);

    HashSet<GameInformation> listGames() throws DataAccessException;
}
