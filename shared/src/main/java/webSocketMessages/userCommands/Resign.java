package webSocketMessages.userCommands;

public class Resign extends UserGameCommand {
    private int gameID;
    private String authToken;
    private CommandType commandType = CommandType.RESIGN;

    public Resign(int gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
    }
}
