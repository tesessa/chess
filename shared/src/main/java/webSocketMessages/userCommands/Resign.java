package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    private int gameID;
    private String auth;
    private CommandType type = CommandType.RESIGN;

    public Resign(int gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
    }
}
