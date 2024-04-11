package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    private int gameID;
    private ChessGame.TeamColor color;
    private CommandType commandType = CommandType.JOIN_PLAYER;
    private String authToken;

    public JoinPlayer(int gameID, ChessGame.TeamColor color, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.color = color;
    }

    public int getGameID() {
        return gameID;
    }

    public String toString() {
        String msg = String.format("%s joined game %d as color %s", authToken,gameID, color);
        return msg;
    }
}
