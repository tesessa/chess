package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private int gameID;
    private ChessMove move;
    private String authToken;
    private CommandType commandType = CommandType.MAKE_MOVE;

    public MakeMove(int gameID, ChessMove move, String authToken) {
        super(authToken);
        this.gameID = gameID;
        this.move = move;
    }
}
