package ui;

import java.util.Scanner;

public class Repl {

    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.printf(EscapeSequences.RESET_BG_COLOR);
        System.out.println("Welcome to Chess!");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            result = client.evaluate(line);
            System.out.printf(result);
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.printf(EscapeSequences.RESET_BG_COLOR + "\n" + ">>> ");
    }

}
