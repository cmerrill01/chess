package ui;

import server.ServerFacade;

import java.util.Arrays;

public class ChessClient {

    private final ServerFacade facade;
    private ClientState state = ClientState.LOGGEDOUT;

    public ChessClient(String serverUrl) {
        facade = new ServerFacade(serverUrl);
    }

    public String evaluate(String input) {
        var tokens = input.toLowerCase().split(" ");
        var command = (tokens.length > 0) ? tokens[0] : "help";
        var parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (command) {
            case "quit" -> "quit";
            case "login" -> login(parameters);
            case "register" -> register(parameters);
            case "logout" -> logout();
            case "create" -> createGame(parameters);
            case "list" -> listGames();
            case "join" -> joinGame(parameters);
            case "observe" -> observeGame(parameters);
            default -> help();
        };
    }

    public String login(String... params) {
        return null;
    }

    public String register(String... params) {
        return null;
    }

    public String logout() {
        return null;
    }

    public String createGame(String... params) {
        return null;
    }

    public String listGames() {
        return null;
    }

    public String joinGame(String... params) {
        return null;
    }

    public String observeGame(String... params) {
        return null;
    }

    public String help() {
        if (state == ClientState.LOGGEDOUT) {
            return """
                    - help
                    - quit
                    - login <username> <password>
                    - register <username> <password> <email>
                    """;
        }
        return """
                - help
                - logout
                - create <game_name>
                - list
                - join <game_id> <WHITE|BLACK>
                - observe <game_id>
                """;
    }

}
