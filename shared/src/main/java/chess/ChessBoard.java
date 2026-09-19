package chess;
import java.util.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.equals(pieceAtPosition, that.pieceAtPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(pieceAtPosition);
    }

    private final Map<ChessPosition, ChessPiece> pieceAtPosition = new HashMap<>();

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        pieceAtPosition.put(position,piece);
    }
    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return pieceAtPosition.get(position);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    //made a note for checkpoint
    public void resetBoard() {
        //clear board
        pieceAtPosition.clear();
        //create back row setup array
        ChessPiece.PieceType[] backRowSetUp = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};
        //insert back row setup at rows 1 and 8 keeping in mind that indexing starts at 0 but board starts at 1 (column - 1)
        for (int column = 1; column <= 8; column++) {
            //add back rows
            addPiece(new ChessPosition(1, column), new ChessPiece(ChessGame.TeamColor.WHITE, backRowSetUp[column - 1]));
            addPiece(new ChessPosition(8, column), new ChessPiece(ChessGame.TeamColor.BLACK, backRowSetUp[column - 1]));
            //add front rows (pawns)
            addPiece(new ChessPosition(2, column), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7, column), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
    }
}
