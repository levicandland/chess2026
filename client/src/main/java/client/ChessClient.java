package client;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class ChessClient {

    private ServerFacade serverFacade;
    private String authToken;
    private HashMap<Integer, Integer> games = new HashMap<>();

    private String preHelp() {
        return "register <USERNAME> <PASSWORD> <EMAIL> - create an account\n"
                + "login <USERNAME> <PASSWORD> - access account\n"
                + "quit - quit program\n"
                + "help - get help\n";
    }

    private String postHelp() {
        return "create <GAME_NAME> - create a game\n"
                + "logout - logout of server\n"
                + "list - list games\n"
                + "play <GAME_NUMBER> <TEAM_COLOR> - join a game\n"
                + "observe <GAME_NUMBER> - watch a game\n"
                + "help - get help\n";
    }

    private String register (String[] tokens) {
        try {
            if (tokens.length != 4) {
                return "Usage: register <USERNAME> <PASSWORD> <EMAIL>";
            }
            AuthData auth = serverFacade.register(new UserData(tokens[1], tokens[2], tokens[3]));
            authToken = auth.authToken();
            return "Registration success. Welcome " + tokens[1];
        }
        catch (ResponseException rE) {
            return "Registration failed: The username may already be taken";
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
            return "Login failed: Wrong username or password";
        }
    }

    private String logout () {
        try {
            serverFacade.logout(authToken);
            authToken = null;
            return "Logout success. Goodbye ";
        }
        catch (ResponseException rE) {
            return "Logout failed: Try again";
        }
    }

    private String create (String[] tokens) {
        try {
            if (tokens.length != 2) {
                return "Usage: create <GAME_NAME>";
            }
            serverFacade.createGame(authToken, tokens[1]);
            return "Game created successfully";
        }
        catch (ResponseException rE) {
            return "Create game failed: Try again";
        }
    }

    private String list () {
        try {
            games.clear();
            Collection<GameData> chessGames = serverFacade.listGames(authToken);
            int gameCounter = 1;
            String output = "";
            String whitePlayer;
            String blackPlayer;
            for (GameData chessGame : chessGames) {
                games.put(gameCounter, chessGame.gameID());
                whitePlayer = playerInterpreter(chessGame.whiteUsername());
                blackPlayer = playerInterpreter(chessGame.blackUsername());
                output = output + gameCounter + " "
                        + chessGame.gameName()
                        + ": White-player = " + whitePlayer
                        + ", Black-player = " + blackPlayer
                        + "\n";
                gameCounter += 1;
            }
            return output;
        }
        catch (ResponseException rE) {
            return "list games failed: Try again";
        }
    }

    private String playerInterpreter (String teamUsername) {
        if (teamUsername == null) {
            return "(empty)";
        }
        else {
            return teamUsername;
        }
    }

    public ChessClient(int port) {
        this.serverFacade = new ServerFacade(port);
    }

    public String preloginEval (String[] tokens) {
        return switch (tokens[0]) {
            case "register" -> register(tokens);
            case "login" -> login(tokens);
            case "help" -> preHelp();
            case "quit" -> "Goodbye";
            default -> "Unknown command. Type help.";
        };
    }

    public String postloginEval (String[] tokens) {
        return switch (tokens[0]) {
            case "logout" -> logout();
            case "create" -> create(tokens);
            case "list" -> list();
            case "help" -> postHelp();
            default -> "Unknown command. Type help.";
        };
    }

    public String eval (String input) {
        String[] tokens = input.split(" ");
        if (!isLoggedIn()) {
            return preloginEval(tokens);
        }
        else {
            return postloginEval(tokens);
        }
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
