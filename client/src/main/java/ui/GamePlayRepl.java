package ui;

import ExceptionClasses.ResponseException;
import WebSocket.GameHandler;
import WebSocket.WebSocketFacade;
import chess.ChessGame;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;

import java.util.Scanner;

public class GamePlayRepl  {
    private final Client client;
    private ChessGame game = new ChessGame();

    public GamePlayRepl(Client client) {
        this.client = client;
    }

    public void run() throws ResponseException {
        Scanner scanner = new Scanner(System.in);
        var result = "";

        while(!result.equals("quit")) {
            if(client.getClientStatus() == 1) {
                PostLoginRepl post = new PostLoginRepl(client);
                post.run();
                break;
            }
           // client.help();
            printPrompt();
            String line = scanner.nextLine();
            try {
                result = client.eval(line);
                System.out.println(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.println(msg);
            }
        }
    }

    public void updateGame(ChessGame newGame) {
        game = newGame;
    }

    public void printMessage(ServerMessage message) {
        if(message.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION || message.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            System.out.println(EscapeSequences.SET_TEXT_COLOR_RED + message.toString());
        }
        if(message.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME) {

        }
        System.out.println(EscapeSequences.SET_TEXT_COLOR_GREEN);
    }

    private void printPrompt() {
        //  EscapeSequences es = new EscapeSequences();
        System.out.print("\n" + EscapeSequences.ERASE_SCREEN + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
