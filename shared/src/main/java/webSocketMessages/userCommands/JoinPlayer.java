package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    private int gameID;
    private String color;
    private CommandType type = CommandType.JOIN_PLAYER;
    private String auth;

    public JoinPlayer(int gameID, String color, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.color = color;
    }

    public int getGameID() {
        return gameID;
    }

    public String getColor() {
        return color;
    }

    public String toString() {
        String msg = String.format("%s joined game %d as color %s", auth,gameID, color);
        return msg;
    }
}
