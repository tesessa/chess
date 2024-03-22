package ui;
import java.util.Scanner;
public class PreLoginRepl {
    private final Client client;
    public PreLoginRepl(String serverUrl) {
        client = new Client(serverUrl);
    }

    public void run() {
        System.out.println(" Welcome to Chess, Type help to get started ");
        //System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while(!result.equals("quit")) {
            if(client.getClientStatus() == 1) {
                PostLoginRepl login = new PostLoginRepl(client);
                login.run();
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
