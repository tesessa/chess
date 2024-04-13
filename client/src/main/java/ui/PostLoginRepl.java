package ui;

import ExceptionClasses.ResponseException;

import java.util.Scanner;

public class PostLoginRepl {
    private final Client client;

    public PostLoginRepl(Client client) {
        this.client = client;
    }

    public void run() throws ResponseException  {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        System.out.println();
        System.out.println(client.help());
        while(!result.equals("quit")) {
            if(client.getClientStatus() == 0) {
                PreLoginRepl preLogin = new PreLoginRepl(client.getServerUrl());
                preLogin.run();
                break;
            }
            if (client.getClientStatus() == 2) {
                GamePlayRepl gamePlay = new GamePlayRepl(client);
                //EscapeSequences print = new EscapeSequences();
               // print.printBoards();
                gamePlay.run();
                break;
            }
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
        System.out.println();
    }

    private void printPrompt() {
        //  EscapeSequences es = new EscapeSequences();
        System.out.print("\n" + EscapeSequences.ERASE_SCREEN + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
