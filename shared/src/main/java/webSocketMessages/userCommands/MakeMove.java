package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private Integer gameID;
    private ChessMove move;
    private String auth;
    private CommandType type = CommandType.MAKE_MOVE;

    public MakeMove(int gameID, ChessMove move, String authToken) {
        super(authToken, CommandType.MAKE_MOVE);
        this.gameID = gameID;
        this.move = move;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }
}
