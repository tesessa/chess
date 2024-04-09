package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {
    private JoinObserver.CommandType commandType = CommandType.JOIN_OBSERVER;
    private String authToken;
    private int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(authToken);
        this.gameID = gameID;
    }
}
