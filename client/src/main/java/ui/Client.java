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
import WebSocket.WebSocketFacade;
import chess.ChessGame;
import com.google.gson.Gson;
import model.UserData;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    private int clientStatus = 0;
    private WebSocketFacade ws;

    private String authToken;

    private String username;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) throws IOException, UnauthorizedException, BadRequestException {
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
                //case "redraw" -> redraw();
                //case "leave" -> leave();
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
            int gameID;
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
            if(color.equals("WHITE")) {
                 join = new JoinGameRequest(ChessGame.TeamColor.WHITE, gameID);
                // ws.joinGame(username,authToken,gameID, ChessGame.TeamColor.WHITE);
            } else {
                 join = new JoinGameRequest(ChessGame.TeamColor.BLACK, gameID);
                // ws.joinGame(username,authToken,gameID, ChessGame.TeamColor.BLACK);
            }
            server.joinGame(join, authToken);
            clientStatus = 2;
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

    public void redraw() {

    }

    public void leave() {
        setClientStatus(1);
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
                    move - makes a move for a piece
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


    //register
    //login
    //quit
    //help
}
