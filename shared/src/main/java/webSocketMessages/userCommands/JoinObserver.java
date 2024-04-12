package webSocketMessages.userCommands;

public class JoinObserver extends UserGameCommand {
    private JoinObserver.CommandType type = CommandType.JOIN_OBSERVER;
    private String auth;
    private int gameID;

    public JoinObserver(String authToken, int gameID) {
        super(authToken, CommandType.JOIN_OBSERVER);
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }
}
