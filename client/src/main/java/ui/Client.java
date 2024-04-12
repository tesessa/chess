package ui;
import java.io.IOException;
import java.util.Arrays;

import ExceptionClasses.BadRequestException;
import ExceptionClasses.ResponseException;
import ExceptionClasses.UnauthorizedException;
import Request.CreateGameRequest;
import Request.JoinGameRequest;
import Request.LoginRequest;
import Result.CreateGameResult;
import Result.ListGameResult;
import Result.RegisterResult;
import Server.ServerFacade;
import WebSocket.GameHandler;
import WebSocket.WebSocketFacade;
import chess.*;
import com.google.gson.Gson;
import model.UserData;
import webSocketMessages.serverMessages.ServerMessage;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    private int clientStatus = 0;
    private WebSocketFacade ws;
    private EscapeSequences draw = new EscapeSequences();
    private String authToken;
    private String username;
    private int gameID;
    private ChessGame.TeamColor playerTeam;
    ChessGame game;
    private EscapeSequences e = new EscapeSequences();

    public Client(String serverUrl) throws ResponseException {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
       // gameHandler g = new GameHandler();
       // ws = new WebSocketFacade(serverUrl, this);
    }

    public String eval(String input) throws IOException, UnauthorizedException, BadRequestException, InvalidMoveException {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "create" -> createGame(params);
                case "list" -> list();
                case "join" -> joinGame(params);
                case "observe" -> joinObserver(params);
                case "logout" -> logout();
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "legal" -> legal(params);
                case "quit" -> "quit";
                default -> help();

            };
        } catch(ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String register(String... params) throws ResponseException, IOException {
        if(params.length == 3) {
            var username = params[0];
            var password = params[1];
            var email = params[2];
            UserData data = new UserData(username, password, email);
            RegisterResult res = server.register(data);
            login(username, password);
            return String.format("You registered as %s " , res.username());

        }
         throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws IOException, ResponseException {
        if (params.length == 2) {
            var user = params[0];
            var password = params[1];
            LoginRequest data = new LoginRequest(user, password);
            RegisterResult result = server.login(data);
            authToken = result.authToken();
            username = result.username();
            clientStatus = 1;
            return String.format("You signed in as %s " , result.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String createGame(String... params) throws ResponseException, IOException, UnauthorizedException {
        assertSignedIn();
        if(params.length == 1) {
            var gameName = params[0];
            CreateGameRequest data = new CreateGameRequest(gameName);
            CreateGameResult result = server.createGame(data, authToken);
            return String.format("You created %s game with ID %d ", gameName, result.gameID());
        }
       throw new ResponseException(400, "Expected: <gameName>");
    }

    public String list() throws ResponseException, UnauthorizedException, IOException {
        assertSignedIn();
        ListGameResult games = server.listGame(authToken);
        var result = new StringBuilder();
        var gson = new Gson();
        for (var game : games.games()) {
            result.append(gson.toJson(game)).append('\n');
        }
        return result.toString();
    }

    public String joinGame(String... params) throws ResponseException, UnauthorizedException, IOException, BadRequestException, NumberFormatException {
        assertSignedIn();
        if(params.length == 1) {
            return joinObserver(params);
        }
        if(params.length == 2) {
           // int id;
            try {
                gameID = Integer.parseInt(params[0]);
            } catch (NumberFormatException ex) {
                throw new ResponseException(400, "You need to put in a valid game ID \nExpected <gameID> <WHITE>|<BLACK>|<empty>");
            }
            //int gameID = Integer.parseInt(params[0]);
            String color = params[1].toUpperCase();
            if(!color.equals("WHITE") && !color.equals("BLACK")) {
                throw new BadRequestException("Expected <gameID> <WHITE>|<BLACK>|<empty>");
            }
            JoinGameRequest join;
           // ws = new WebSocketFacade(serverUrl);
            ChessGame.TeamColor playerColor;
            if(color.equals("WHITE")) {
                 join = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);
                 playerColor = ChessGame.TeamColor.WHITE;
            } else {
                 join = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
                 playerColor = ChessGame.TeamColor.BLACK;
            }
            server.joinGame(join, authToken);
            clientStatus = 2;
            ws = new WebSocketFacade(serverUrl, this);
            ws.joinPlayer(authToken, gameID, playerColor);
            playerTeam = playerColor;
            //redraw();
            return String.format("You joined game %d as %s on team %s", gameID, username, color);
        }
        throw new ResponseException(400, "Expected <gameID> <WHITE>|<BLACK>|<empty>");
    }

    public String joinObserver(String... params) throws ResponseException, UnauthorizedException, IOException {
        assertSignedIn();
        if(params.length == 1) {
            int gameID = Integer.parseInt(params[0]);
            JoinGameRequest join = new JoinGameRequest(null, gameID);
            server.joinGame(join, authToken);
            clientStatus = 2;
            ws = new WebSocketFacade(serverUrl, this);
            ws.joinObserver(authToken, gameID);
            //redraw();
            return String.format("%s joined game %d as an observer", username, gameID);
        }
        throw new ResponseException(400, "Expected <gameID>");
    }

    public String logout() throws UnauthorizedException, IOException {
        assertSignedIn();
        server.logout(authToken);
        String result = String.format("You logged out as %s", username);
        username = null;
        authToken = null;
        clientStatus = 0;
        return result;
    }

    public String redraw() {
     //   System.out.println("Redraw");
        int[][] arr = new int[8][8];
        //EscapeSequences e = new EscapeSequences();
        if(playerTeam == ChessGame.TeamColor.BLACK) {
            e.printBlackBoard(game.getBoard(), arr);
        } else {
            e.printWhiteBoard(game.getBoard(),arr);
        }
        //e.printWhiteBoard(new ChessBoard(), arr);
        return "";
    }

    public String leave() throws ResponseException {
        setClientStatus(1);
        ws.leave(authToken, gameID);
        //ws = null;
        String result = String.format("You left game %d as %s", gameID, username);
        return result;
    }

    public String move(String... params) throws ResponseException {
        if(params.length == 2) {
            String startPosition = params[0].toUpperCase();
            String endPosition = params[1].toUpperCase();
            ChessPosition start = convertInputPosition(startPosition);
            ChessPosition end = convertInputPosition(endPosition);
            ChessPosition startFinal;
            ChessPosition endFinal;
            if(playerTeam == ChessGame.TeamColor.BLACK) {
                 startFinal = new ChessPosition(9 - start.getRow(), start.getColumn());
                 endFinal = new ChessPosition(9 - end.getRow(), end.getColumn());
            } else {
                 startFinal = start;
                 endFinal = end;
            }
            System.out.println(game.toString());
            System.out.println("Team " + playerTeam + " start position " + startPosition + start + " end position " + endPosition + end);
            ChessMove movePiece = new ChessMove(startFinal, endFinal, null);
            ws.makeMove(gameID, movePiece, authToken);
            ws = new WebSocketFacade(serverUrl, this);
            redraw();
            return "";
        }
        throw new ResponseException(500, "Expected <start-position> <end-position>");
    }

    public String resign(String... params) throws ResponseException {
        ws.resign(gameID, authToken);
        String result = String.format("You resigned game %d as %s", gameID, username);
        return result;
    }

    public String legal(String... params) throws ResponseException, InvalidMoveException {
        if(params.length == 1) {
            String p = params[0].toUpperCase();
            ChessPosition temp = convertInputPosition(p);
            ChessPosition position;
            if(playerTeam == ChessGame.TeamColor.WHITE) {
                position = new ChessPosition(temp.getRow(), 9-temp.getColumn());
            } else {
                position = temp;
            }
            e.printLegalMoves(game, position, playerTeam);
            return "";
        }
        throw new ResponseException(500, "Expected <POSITION>");
    }

    private ChessPosition convertInputPosition(String position) throws ResponseException {
        int col;
        int row;
        if(position.length() != 2) {
            throw new ResponseException(500, "You need to input column and row");
        }
        if(position.charAt(0) == 'A') col = 1; //8
        else if(position.charAt(0) == 'B') col = 2; //7
        else if(position.charAt(0) == 'C') col = 3; //6
        else if(position.charAt(0) == 'D') col = 4; //5
        else if(position.charAt(0) == 'E') col = 5; //4
        else if(position.charAt(0) == 'F') col = 6; //3
        else if(position.charAt(0) == 'G') col = 7; //2
        else if(position.charAt(0) == 'H') col = 8; //1
        else {
            throw new ResponseException(500, "Not a valid column value");
        }
        if(position.charAt(1) == '1') row = 1;
        else if(position.charAt(1) == '2') row = 2;
        else if(position.charAt(1) == '3') row = 3;
        else if(position.charAt(1) == '4') row = 4;
        else if(position.charAt(1) == '5') row = 5;
        else if(position.charAt(1) == '6') row = 6;
        else if(position.charAt(1) == '7') row = 7;
        else if(position.charAt(1) == '8') row = 8;
        else {
            throw new ResponseException(500, "Invalid row value");
        }
        /*ChessPosition chessPosition;
        if(playerTeam == ChessGame.TeamColor.WHITE) {
            chessPosition = new ChessPosition(row, col);
        } else {
            chessPosition = new ChessPosition(row, col);
        }*/
        ChessPosition chessPosition = new ChessPosition(row, col);
        return chessPosition;
    }

    public int getClientStatus() {
        return  clientStatus;
    }

    public void setClientStatus(int status) {
        clientStatus = status;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String help() {
        if(clientStatus == 1) {
            return """
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE|BLACK|<empty>] - a game
                    observe <ID> - a game
                    logout - when you are done
                    quit - playing chess
                    help - with possible commands
                    """;
        } else if (clientStatus == 2) {
            return """
                    redraw - redraws the chessboard
                    leave - leave the game
                    move <start-position> <end-position> - makes a move for a piece 
                      (example e2 e3)
                    resign - forfeit game and game is over
                    legal - moves you can make
                    help - with possible commands
                    """;
        }
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
    }

    public void assertSignedIn() throws UnauthorizedException {
        if(clientStatus == 0) {
            throw new UnauthorizedException();
        }
    }

    public void updateGame(ChessGame newGame) {
        System.out.println("Hey");
        game = newGame;
        redraw();

    }

    public void printMessage(String message, ServerMessage s) {
        if(s.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION || s.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message);
        }
        if(s.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {

        }
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN);
    }


    //register
    //login
    //quit
    //help
}
