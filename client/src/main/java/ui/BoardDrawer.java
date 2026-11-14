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
                return switch (piece.getPieceType()) {
                    case KING -> EscapeSequences.WHITE_KING;
                    case QUEEN -> EscapeSequences.WHITE_QUEEN;
                    case BISHOP -> EscapeSequences.WHITE_BISHOP;
                    case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
                    case ROOK -> EscapeSequences.WHITE_ROOK;
                    case PAWN -> EscapeSequences.WHITE_PAWN;
                };
            }
            else {
                return switch (piece.getPieceType()) {
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
        System.out.println("   a   b   c  d   e  f   g   h ");
        for (int row = 8; row >= 1; row--) {
            System.out.print(row + " ");
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if ((row + col) %2 == 0) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
                }
                else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                }
                System.out.print(pieceInterpreter(piece));
                System.out.print(EscapeSequences.RESET_BG_COLOR);
            }
            System.out.print("\n");
        }
    }
}
