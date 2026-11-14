package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

public class BoardDrawer {

    private static String pieceInterpreter (ChessPiece piece) {
        if (piece == null) {
            return EscapeSequences.EMPTY;
        }
        else {
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                return EscapeSequences.SET_TEXT_COLOR_WHITE + switch (piece.getPieceType()) {
                    case KING -> EscapeSequences.WHITE_KING;
                    case QUEEN -> EscapeSequences.WHITE_QUEEN;
                    case BISHOP -> EscapeSequences.WHITE_BISHOP;
                    case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                    case ROOK -> EscapeSequences.WHITE_ROOK;
                    case PAWN -> EscapeSequences.WHITE_PAWN;
                };
            }
            else {
                return EscapeSequences.SET_TEXT_COLOR_BLACK + switch (piece.getPieceType()) {
                    case KING -> EscapeSequences.BLACK_KING;
                    case QUEEN -> EscapeSequences.BLACK_QUEEN;
                    case BISHOP -> EscapeSequences.BLACK_BISHOP;
                    case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                    case ROOK -> EscapeSequences.BLACK_ROOK;
                    case PAWN -> EscapeSequences.BLACK_PAWN;
                };
            }
        }
    }

    public static void drawBoard(ChessBoard board, ChessGame.TeamColor color) {
        //check perspective
        boolean isWhite = (color == ChessGame.TeamColor.WHITE);
        //header relative to perspective
        if (isWhite) {
            System.out.println("   a   b   c  d   e  f   g   h ");
        }
        else {
            System.out.println("   h   g   f  e   d  c   b   a ");
        }
        //row relative to perspective
        for (int i = 1; i <= 8; i++) {
            int row;
            if (isWhite) {
                row = 9 - i;
            }
            else {
                row = i;
            }
            System.out.print(row + " ");
            //col relative to perspective
            for (int j = 1; j <= 8; j++) {
                int col;
                if (isWhite) {
                    col = j;
                }
                else {
                    col = 9 - j;
                }
                //pieces and squares colors
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if ((row + col) %2 == 0) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                }
                else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }
                System.out.print(pieceInterpreter(piece));
                System.out.print(EscapeSequences.RESET_BG_COLOR);
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
            System.out.print("\n");
        }
    }
}
