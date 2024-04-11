package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand {
    private int gameID;
    private ChessGame.TeamColor playerColor;
    private CommandType type = CommandType.JOIN_PLAYER;
    private String auth;

    public JoinPlayer(int gameID, ChessGame.TeamColor playerColor, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessGame.TeamColor getColor() {
        return playerColor;
    }

    public String toString() {
        String msg = String.format("%s joined game %d as color %s", auth,gameID, playerColor);
        return msg;
    }
}
