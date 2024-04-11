package dataAccess;

import Result.GameInformation;
import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.HashSet;

public class MemoryGameDataAccess implements GameDataAccess {

    private int gameID = 0;
    HashMap<Integer, GameData> gd = new HashMap<Integer, GameData>();
    HashSet<GameData> games = new HashSet<GameData>();
    HashSet<GameInformation> printGames = new HashSet<GameInformation>();
    public void clear() {
        gd.clear();
        games.clear();
        printGames.clear();
        gameID = 0;
    }

    public int createGame(String gameName) {
        gameID++;
        ChessGame game = new ChessGame();
        GameData data = new GameData(gameID, null, null, gameName, game);
        gd.put(gameID, data);
        games.add(data);
        printGames.add(new GameInformation(gameID, null, null, gameName));
        return gameID;
    }

    public GameData getGame(int gameID) {
        Integer temp = Integer.valueOf(gameID);
        return gd.get(temp);
    }

    public HashSet<GameInformation> listGames() {
        return printGames;
    }

    public void updateGame(GameData game, String username, ChessGame.TeamColor color) {
        GameData newData;
        GameInformation temp = new GameInformation(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
        GameInformation newList;
        if(color == ChessGame.TeamColor.BLACK) {
            newData = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            newList = new GameInformation(game.gameID(), game.whiteUsername(), username, game.gameName());
        } else if(color == ChessGame.TeamColor.WHITE) {
            newData = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            newList = new GameInformation(game.gameID(), username, game.blackUsername(), game.gameName());
        } else {
            newData = game;
            newList = temp;
        }
        games.remove(game);
        printGames.remove(temp);
        gd.remove(game.gameID());
        gd.put(newData.gameID(), newData);
        games.add(newData);
        printGames.add(newList);
    }

    public void updateBoard(GameData data) {

    }
}
