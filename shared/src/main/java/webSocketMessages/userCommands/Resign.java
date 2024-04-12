package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    private Integer gameID;
    private String auth;
    private CommandType type = CommandType.RESIGN;

    public Resign(int gameID, String authToken) {
        super(authToken, CommandType.RESIGN);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
