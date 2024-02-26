package dataAccess;

import Result.GameInformation;
import chess.ChessGame;
import model.GameData;

import java.util.HashSet;

public interface GameDataAccess {
    void clear();

    GameData getGame(int gameID);

    int createGame(String gameName);

    void updateGame(GameData game, String username, ChessGame.TeamColor color);

    HashSet<GameInformation> listGames();
}
