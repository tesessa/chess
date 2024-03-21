package ui;
import java.io.IOException;
import java.util.Arrays;

import ExceptionClasses.ResponseException;
import Request.LoginRequest;
import Result.RegisterResult;
import Server.ServerFacade;
import model.UserData;

import javax.imageio.IIOException;

public class PreloginUI {
    private final ServerFacade server;
    private final String serverUrl;

    public PreloginUI(String serverUrl) {
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

    public String login(String... params) {
        if (params.length == 2) {
            var username = params[0];
            var password = params[1];
            LoginRequest data = new LoginRequest(username, password);
            
        }
        return "";
    }

    public String help() {
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
