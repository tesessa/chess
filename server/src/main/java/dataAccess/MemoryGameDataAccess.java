package dataAccess;

import model.GameData;

import java.util.HashMap;

public class MemoryGameDataAccess implements GameDataAccess {

    HashMap<String, GameData> gd = new HashMap<String, GameData>();
    public void clear() {
        gd.clear();
    }
}
