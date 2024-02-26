package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDataAccess implements GameDataAccess {

    private int gameID = 0;
    HashMap<Integer, GameData> gd = new HashMap<Integer, GameData>();
    HashSet<GameData> games = new HashSet<GameData>();
    public void clear() {
        gd.clear();
        games.clear();
        gameID = 0;
    }

    public int createGame(String gameName) {
        gameID++;
        ChessGame game = new ChessGame();
        GameData data = new GameData(gameID, null, null, gameName, game);
        gd.put(gameID, data);
        games.add(data);
        return gameID;
    }
}
