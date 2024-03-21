package ui;
import java.io.IOException;
import java.util.Arrays;

import ExceptionClasses.AlreadyTakenException;
import ExceptionClasses.BadRequestException;
import ExceptionClasses.ResponseException;
import ExceptionClasses.UnauthorizedException;
import Request.LoginRequest;
import Result.RegisterResult;
import Server.ServerFacade;
import model.UserData;

public class Client {
    private final ServerFacade server;
    private final String serverUrl;
    private int clientStatus = 0;

    public Client(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String eval(String input) throws IOException {
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
            return String.format("You registered as %s " , res.username());

        }
         throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String login(String... params) throws IOException, ResponseException {
        if (params.length == 2) {
            var username = params[0];
            var password = params[1];
            LoginRequest data = new LoginRequest(username, password);
            RegisterResult result = server.login(data);
            clientStatus = 1;
            return String.format("You signed in as %s " , result.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String createGame(String... params) throws ResponseException {
       return "";
    }

    public String list() throws ResponseException {
        return "";
    }

    public String joinGame(String... params) throws ResponseException {
        return "";
    }

    public String joinObserver(String... params) throws ResponseException {
        return "";
    }

    public String logout() {
        return "";
    }

    public int getClientStatus() {
        return  clientStatus;
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
        }
        return """
                register <USERNAME> <PASSWORD> <EMAIL> - to create an account
                login <USERNAME> <PASSWORD> - to play chess
                quit - playing chess
                help - with possible commands
                """;
    }

    //register
    //login
    //quit
    //help
}
