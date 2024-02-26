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

    public GameData getGame(int gameID) {
        Integer temp = Integer.valueOf(gameID);
        return gd.get(temp);
    }

    public void updateGame(GameData game, String username, ChessGame.TeamColor color) {
        GameData newData;
        if(color == ChessGame.TeamColor.BLACK) {
            newData = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
        } else if(color == ChessGame.TeamColor.WHITE) {
            newData = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
        } else {
            newData = game;
        }
        for(GameData loop : games) {
            if(loop.equals(game)) {
                games.remove(game);
            }
        }
        gd.remove(game.gameID());
        gd.put(newData.gameID(), newData);
        games.add(newData);
    }
}
