package client;

import model.AuthData;
import model.UserData;

public class ChessClient {

    private ServerFacade serverFacade;
    private String authToken;

    private String help() {
        return "register <USERNAME> <PASSWORD> <EMAIL> - create an account\n"
                + "login <USERNAME> <PASSWORD> - access account\n"
                + "quit - quit program\n"
                + "help - get help\n";
    }

    private String register (String[] tokens) {
        try {
            if (tokens.length != 4) {
                return "Usage: register <USERNAME> <PASSWORD> <EMAIL>";
            }
            AuthData auth = serverFacade.register(new UserData(tokens[1], tokens[2], tokens[3]));
            authToken = auth.authToken();
            return "registration success. Welcome " + tokens[1];
        }
        catch (ResponseException rE) {
            return "registration failed: The username may already be taken";
        }
    }

    private String login (String[] tokens) {
        try {
            if (tokens.length != 3) {
                return "Usage: login <USERNAME> <PASSWORD>";
            }
            AuthData auth = serverFacade.login(tokens[1], tokens[2]);
            authToken = auth.authToken();
            return "Login success. Welcome " + tokens[1];
        }
        catch (ResponseException rE) {
            return "login failed: Wrong username or password";
        }
    }

    public ChessClient(int port) {
        this.serverFacade = new ServerFacade(port);
    }

    public String eval (String input) {
        String[] tokens = input.split(" ");
        return switch (tokens[0]) {
            case "register" -> register(tokens);
            case "login" -> login(tokens);
            case "help" -> help();
            default -> "Unknown command. Type help.";
        };
    }

    public boolean isLoggedIn () {
        if (authToken != null) {
            return true;
        }
        else {
            return false;
        }
    }
}
