package webSocketMessages.userCommands;

public class Leave extends UserGameCommand {
    private int gameID;
    private CommandType commandType = CommandType.LEAVE;
    private String authToken;

    public Leave(int gameID, String authToken) {
        super(authToken);
        this.gameID = gameID;
    }
}
