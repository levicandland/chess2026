package ui;

import chess.ChessBoard;
import chess.ChessGame;

public class BoardDrawer {
    public static void drawBoard(ChessBoard board, ChessGame.TeamColor color) {
        System.out.println("   a  b  c  d  e  f  g  h ");
        for (int row = 8; row >= 1; row--) {
            System.out.print(row + " ");
            for (int col = 1; col <= 8; col++) {
                if ((row + col) %2 == 0) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                }
                else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }
                System.out.print(" . ");
                System.out.print(EscapeSequences.RESET_BG_COLOR);
            }
            System.out.print("\n");
        }
    }
}
