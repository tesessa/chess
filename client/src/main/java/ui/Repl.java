package ui;
import java.util.Scanner;
public class Repl {
    private final PreloginUI client;

    public Repl(String serverUrl) {
        client = new PreloginUI(serverUrl);
    }

    public void run() {
        System.out.println(" Welcome to Chess, Register to start ");
        //System.out.print(client.help());

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
        System.out.println();
    }



    private void printPrompt() {
      //  EscapeSequences es = new EscapeSequences();
        System.out.print("\n" + EscapeSequences.ERASE_SCREEN + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
