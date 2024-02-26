package dataAccess;

import chess.ChessGame;
import model.GameData;

public interface GameDataAccess {
    void clear();

    GameData getGame(int gameID);

    int createGame(String gameName);

    void updateGame(GameData game, String username, ChessGame.TeamColor color);
}
