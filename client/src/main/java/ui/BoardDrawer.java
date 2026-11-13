package ui;

import chess.ChessBoard;
import chess.ChessGame;

public class BoardDrawer {
    public static void drawBoard(ChessBoard board, ChessGame.TeamColor color) {
        System.out.println("   a  b  c  d  e  f  g  h ");
        for (int row = 8; row >= 1; row--) {
            System.out.print(row + " ");
            for (int col = 1; col <= 8; col++) {
                System.out.print(" . ");
            }
            System.out.print("\n");
        }
    }
}
