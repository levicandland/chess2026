package client;

import chess.*;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("♕ Welcome 240 Chess Client");
        Scanner scanner = new Scanner(System.in);
        String input = "";
        ChessClient client = new ChessClient (8080);
        while (!input.equals("quit")){
            if (client.isLoggedIn()) {
                System.out.print("[LOGGED_IN]>>> ");
            }
            else {
                System.out.print("[LOGGED_OUT or NOT_REGISTERED]>>> ");
            }
            input = scanner.nextLine();
            System.out.println(client.eval(input));
        }
    }
}
