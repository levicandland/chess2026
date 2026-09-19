package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    private final ChessGame.TeamColor pieceColor;
    private final ChessPiece.PieceType type;

    //knight helper function
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){

        Collection<ChessMove> moves = new ArrayList<>();
        int [][] knightOffsets = {{1,2}, {1,-2}, {-1,2}, {-1,-2}, {2,1}, {2,-1}, {-2,1}, {-2,-1}};

        for (int[] offset : knightOffsets) {

            int candidateRow = offset[0] + myPosition.getRow();
            int candidateColumn = offset[1] + myPosition.getColumn();

            if ((candidateRow >= 1 && candidateRow <= 8) && (candidateColumn >= 1 && candidateColumn <= 8)) {

                ChessPosition candidateSquare = new ChessPosition(candidateRow, candidateColumn);
                ChessPiece piece = board.getPiece(candidateSquare);

                if ((piece == null) || (piece.getTeamColor() != this.getTeamColor())) {

                    ChessMove moveKnight = new ChessMove(myPosition, candidateSquare, null);
                    moves.add(moveKnight);
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition){

        Collection<ChessMove> moves = new ArrayList<>();
        int [][] kingOffsets = {{1,1}, {1,-1}, {-1,1}, {-1,-1}, {0,1}, {0,-1}, {-1,0}, {1,0}};

        for (int[] offset : kingOffsets) {

            int candidateRow = offset[0] + myPosition.getRow();
            int candidateColumn = offset[1] + myPosition.getColumn();

            if ((candidateRow >= 1 && candidateRow <= 8) && (candidateColumn >= 1 && candidateColumn <= 8)) {

                ChessPosition candidateSquare = new ChessPosition(candidateRow, candidateColumn);
                ChessPiece piece = board.getPiece(candidateSquare);

                if ((piece == null) || (piece.getTeamColor() != this.getTeamColor())) {

                    ChessMove moveKing = new ChessMove(myPosition, candidateSquare, null);
                    moves.add(moveKing);
                }
            }
        }

        return moves;
    }

    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition myPosition, int [][] directions){
        Collection<ChessMove> moves = new ArrayList<>();
        for (int [] direction : directions) {
            int currentRow = myPosition.getRow();
            int currentColumn = myPosition.getColumn();
            while (true) {
                currentRow += direction[0];
                currentColumn += direction[1];

                if (!(currentRow >= 1 && currentRow <= 8) || !(currentColumn >= 1 && currentColumn <= 8)) {
                    break;
                }

                ChessPosition currentSquare = new ChessPosition(currentRow, currentColumn);
                ChessPiece occupant = board.getPiece(currentSquare);

                if (occupant == null){
                    ChessMove slide = new ChessMove(myPosition, currentSquare, null);
                    moves.add(slide);
                    continue;
                }
                else if (occupant.getTeamColor() != this.getTeamColor()){
                    ChessMove slide = new ChessMove(myPosition, currentSquare, null);
                    moves.add(slide);
                    break;
                }
                else {
                    break;
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
        int [][] rookDirections = {{0,1}, {0,-1}, {1,0}, {-1,0}};
        return slidingMoves(board, myPosition, rookDirections);
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition){
        int [][] bishopDirections = {{1,1}, {1,-1}, {-1,1}, {-1,-1}};
        return slidingMoves(board, myPosition, bishopDirections);
    }

    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition){
        int [][] queenDirections = {{0,1}, {0,-1}, {1,0}, {-1,0}, {1,1}, {1,-1}, {-1,1}, {-1,-1}};
        return slidingMoves(board, myPosition, queenDirections);
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> moves = new ArrayList<>();
        //declare variables for pawns
        int direction;
        int startingRow;
        int promotionRow;
        //initialize respective variables for WHITE and BLACK pawns
        if(this.getTeamColor() == ChessGame.TeamColor.WHITE){
            direction = 1;
            startingRow = 2;
            promotionRow = 8;

        }
        else{
            direction = -1;
            startingRow = 7;
            promotionRow = 1;
        }

        //declare needed variables for one-step
        int oneStepRow = myPosition.getRow() + direction;
        ChessPosition oneStepSquare = new ChessPosition(oneStepRow, myPosition.getColumn());
        ChessPiece occupantForwardOne = board.getPiece(oneStepSquare);

        //declare needed variables for two-step
        int twoStepRow = myPosition.getRow() + (direction * 2);
        ChessPosition twoStepSquare = new ChessPosition(twoStepRow, myPosition.getColumn());
        ChessPiece occupantForwardTwo = board.getPiece(twoStepSquare);

        //declare needed variables for diagonal capture
        int [] [] pawnOffSets = {{direction, 1}, {direction, -1}};

        //if nothing in first square
        if (occupantForwardOne == null) {
            //check promotion
            checkAddPromotions(moves, myPosition, oneStepSquare, promotionRow);
            //check double step
            if (myPosition.getRow() == startingRow) {
                if (occupantForwardTwo == null) {
                    ChessMove pawnTwoStep = new ChessMove(myPosition, twoStepSquare, null);
                    moves.add(pawnTwoStep);
                }
            }
        }

        //diagonal pawn capture
        for (int[] offSet : pawnOffSets) {

            int candidateRow = offSet[0] + myPosition.getRow();
            int candidateColumn = offSet[1] + myPosition.getColumn();

            if ((candidateRow >= 1 && candidateRow <= 8) && (candidateColumn >= 1 && candidateColumn <= 8)) {

                ChessPosition candidateSquare = new ChessPosition(candidateRow, candidateColumn);
                ChessPiece piece = board.getPiece(candidateSquare);

                if ((piece != null) && (piece.getTeamColor() != this.getTeamColor())) {

                    checkAddPromotions(moves, myPosition, candidateSquare, promotionRow);
                }
            }
        }

        return moves;

    }

    private void checkAddPromotions (Collection<ChessMove> moves, ChessPosition start, ChessPosition end, int promotionRow) {
        if (end.getRow() == promotionRow) {
            ChessPiece.PieceType[] pawnPromotionPieces = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.QUEEN};
            //promotion
            for (ChessPiece.PieceType promoPiece : pawnPromotionPieces ) {
                ChessMove pawnPromotion = new ChessMove(start, end, promoPiece);
                moves.add(pawnPromotion);
            }
        }
        else {
            ChessMove normalPawnMove = new ChessMove(start, end, null);
            moves.add(normalPawnMove);
        }
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        switch (getPieceType()){
            case KING:
                return kingMoves(board, myPosition);
            case QUEEN:
                return queenMoves(board, myPosition);
            case BISHOP:
                return bishopMoves(board, myPosition);
            case KNIGHT:
                return knightMoves(board, myPosition);
            case ROOK:
                return rookMoves(board, myPosition);
            case PAWN:
                return pawnMoves(board, myPosition);
            default:
                throw new RuntimeException("Not implemented");
        }

    }
}
