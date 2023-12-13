package ui;

import webSocketMessages.serverMessages.ServerMessage;
import websocket.NotificationHandler;

import java.util.Scanner;

public class Repl implements NotificationHandler {

    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.printf(EscapeSequences.RESET_BG_COLOR + EscapeSequences.SET_TEXT_COLOR_WHITE);
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
        System.out.printf(EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.RESET_BG_COLOR + "\n" + client.getState() + " >>> ");
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.printf(EscapeSequences.SET_TEXT_COLOR_RED + EscapeSequences.RESET_BG_COLOR + notification.toString());
        printPrompt();
    }
}
