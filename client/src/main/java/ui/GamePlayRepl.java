package ui;

import java.util.Scanner;

public class GamePlayRepl {
    private final Client client;
    public GamePlayRepl(Client client) {
        this.client = client;
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {

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

    private void printPrompt() {
        //  EscapeSequences es = new EscapeSequences();
        System.out.print("\n" + EscapeSequences.ERASE_SCREEN + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
