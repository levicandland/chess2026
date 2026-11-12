package client;

import chess.*;

import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        System.out.println("♕ Welcome 240 Chess Client");
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equals("quit")){
            System.out.print("[LOGGED_OUT or NOT_REGISTERED]>>> ");
            input = scanner.nextLine();
        }
    }
}
