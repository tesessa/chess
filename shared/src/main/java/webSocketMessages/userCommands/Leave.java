package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    private int gameID;
    private CommandType type = CommandType.LEAVE;
    private String auth;

    public Leave(int gameID, String authToken) {
        super(authToken, CommandType.LEAVE);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
