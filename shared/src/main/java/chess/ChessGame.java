package chess;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;
import java.util.HashSet;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }

    private Collection<ChessMove> castlingMoves (ChessPosition myPosition ){
        //collection for possible castling moves
        Collection<ChessMove> castleMoves = new ArrayList<>();
        //make sure king is in starting position
        ChessPiece king = board.getPiece(myPosition);
        int homeRow;

        if (king.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
            homeRow = 1;
        }
        else {
            homeRow = 8;
        }

        ChessPosition kingHomeSqr = new ChessPosition(homeRow, 5);

        if (!(myPosition.equals(kingHomeSqr))) {
            return castleMoves;
        }
        //if king has moved
        if (playedSquares.contains(myPosition)) {
            return castleMoves;
        }
        //if king is in check
        if (isInCheck(king.getTeamColor())) {
            return castleMoves;
        }
        //Rook checks
        //Is the king side rook home and unmoved?
        ChessPosition ksRookSqr = new ChessPosition(homeRow, 8);
        ChessPiece ksOccupant = board.getPiece(ksRookSqr);

        if ((ksOccupant != null)
                && (ksOccupant.getPieceType().equals(ChessPiece.PieceType.ROOK))
                && (ksOccupant.getTeamColor().equals(king.getTeamColor()))
                && !(playedSquares.contains(ksRookSqr))) {
            ChessPosition ksSqrOne = new ChessPosition(homeRow, 6);
            ChessPosition ksSqrTwo = new ChessPosition(homeRow, 7);
            ChessPiece ksSqrOneOccupant = board.getPiece(ksSqrOne);
            ChessPiece ksSqrTwoOccupant = board.getPiece(ksSqrTwo);

            if ((ksSqrOneOccupant == null) && (ksSqrTwoOccupant == null)) {
                //make a copy board to analyse that move
                ChessBoard testBoard = copyBoard(board);
                //start test
                //vacate startPosition
                testBoard.addPiece(myPosition, null);
                //occupy endPosition
                testBoard.addPiece(ksSqrOne, king);
                //test and validate move
                if (!(isInCheckHelper(king.getTeamColor(), testBoard))) {
                    ChessMove ksCastle = new ChessMove(myPosition, ksSqrTwo, null);
                    castleMoves.add(ksCastle);
                }
            }
        }

        //Is the queen side rook home and unmoved?
        ChessPosition qsRookSqr = new ChessPosition(homeRow, 1);
        ChessPiece qsOccupant = board.getPiece(qsRookSqr);

        if ((qsOccupant != null)
                && (qsOccupant.getPieceType().equals(ChessPiece.PieceType.ROOK))
                && (qsOccupant.getTeamColor().equals(king.getTeamColor()))
                && !(playedSquares.contains(qsRookSqr))) {
            ChessPosition qsSqrOne = new ChessPosition(homeRow, 4);
            ChessPosition qsSqrTwo = new ChessPosition(homeRow, 3);
            ChessPosition qsSqrThree = new ChessPosition(homeRow, 2);
            ChessPiece qsSqrOneOccupant = board.getPiece(qsSqrOne);
            ChessPiece qsSqrTwoOccupant = board.getPiece(qsSqrTwo);
            ChessPiece qsSqrThreeOccupant = board.getPiece(qsSqrThree);

            if ((qsSqrOneOccupant == null) && (qsSqrTwoOccupant == null)
                    && (qsSqrThreeOccupant == null)) {
                //make a copy board to analyse that move
                ChessBoard testBoard = copyBoard(board);
                //start test
                //vacate startPosition
                testBoard.addPiece(myPosition, null);
                //occupy endPosition
                testBoard.addPiece(qsSqrOne, king);
                //test and validate move
                if (!(isInCheckHelper(king.getTeamColor(), testBoard))) {
                    ChessMove qsCastle = new ChessMove(myPosition, qsSqrTwo, null);
                    castleMoves.add(qsCastle);
                }
            }
        }
        return castleMoves;
    }

    private void moveCastlingRook (ChessMove move, ChessBoard boardSub) {
        ChessPiece castlingKing = boardSub.getPiece(move.getStartPosition());
        if ((castlingKing.getPieceType().equals(ChessPiece.PieceType.KING))
                && (Math.abs(move.getStartPosition().getColumn() - move.getEndPosition().getColumn()) ==  2)) {
            int rookStartCol;
            int rookEndCol;
            //kingside vs queenside  vars
            if (move.getEndPosition().getColumn() == 7) {
                rookStartCol = 8;
                rookEndCol = 6;
            }
            else {
                rookStartCol = 1;
                rookEndCol = 4;
            }
            //rest of vars
            int castlingRow = move.getStartPosition().getRow();
            ChessPosition rookStartSqr = new ChessPosition(castlingRow, rookStartCol);
            ChessPosition rookEndSqr = new ChessPosition(castlingRow, rookEndCol);
            ChessPiece castlingRook = boardSub.getPiece(rookStartSqr);
            //move rook
            //empty startSqr
            boardSub.addPiece(rookStartSqr, null);
            //pur rook on castling rook endSqr
            boardSub.addPiece(rookEndSqr, castlingRook);
        }
    }

    private void removeEnPassantCapture (ChessMove move, ChessBoard boardSub) {
        ChessPiece enPassantPredator = boardSub.getPiece(move.getStartPosition());
        if ((enPassantPredator.getPieceType().equals(ChessPiece.PieceType.PAWN))
                &&( move.getStartPosition().getColumn() != move.getEndPosition().getColumn())
                &&(boardSub.getPiece(move.getEndPosition()) == null)) {
            ChessPosition enPassCaptureSqr = new ChessPosition(move.getStartPosition().getRow(), move.getEndPosition().getColumn());
            boardSub.addPiece(enPassCaptureSqr, null);
        }
    }

    private ChessMove enPassant (ChessPosition myPosition){
        //if there was no last move
        if (lastMove == null) {
            return null;
        }
        //who's last mover, who's potential en Passer
        ChessPiece lastMover = board.getPiece(lastMove.getEndPosition());
        ChessPiece thisPawn = board.getPiece(myPosition);
        //if lastMover is not Pawn or lasMover is on same team
        if ((lastMover.getPieceType() != ChessPiece.PieceType.PAWN) || (lastMover.getTeamColor() == thisPawn.getTeamColor())) {
            return null;
        }
        //if the abs value of difference between last moves start and end position is not 2
        if (Math.abs(lastMove.getStartPosition().getRow() - lastMove.getEndPosition().getRow()) != 2) {
            return null;
        }
        //if they arent on same row
        if (lastMove.getEndPosition().getRow() != myPosition.getRow()) {
            return null;
        }
        //check side by side
        if (Math.abs(lastMove.getEndPosition().getColumn() - myPosition.getColumn()) != 1) {
            return null;
        }
        //otherwise move diagonal beyond other pawn and other pawn goes out
        int enPassantCol = lastMove.getEndPosition().getColumn();
        int enPassantRow;
        if (thisPawn.getTeamColor() == TeamColor.WHITE) {
            enPassantRow = lastMove.getEndPosition().getRow() + 1;
        }
        else {
            enPassantRow = lastMove.getEndPosition().getRow() - 1;
        }
        //
        ChessPosition enPassSqr = new ChessPosition(enPassantRow, enPassantCol);
        return new ChessMove(myPosition, enPassSqr, null);
    }

    private boolean hasValidMoves (ChessGame.TeamColor teamColor){
        for (int row = 1; row <= 8; row ++){
            for (int col = 1; col <= 8; col ++){
                ChessPosition curSqr = new ChessPosition(row, col);
                ChessPiece occupant = board.getPiece(curSqr);
                if ((occupant != null) && (occupant.getTeamColor().equals(teamColor))) {
                    if (!(validMoves(curSqr).isEmpty())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isInCheckHelper(TeamColor teamColor, ChessBoard boardSub) {
        //check if king is found
        ChessPosition kingFound = findKing(teamColor, boardSub);
        if (kingFound == null){
            return false;
        }
        //can enemy attack?
        for (int row = 1; row <= 8; row ++){
            for (int col = 1; col <= 8; col ++){
                ChessPosition canKingEnemy = new ChessPosition(row, col);
                ChessPiece occupant = boardSub.getPiece(canKingEnemy);
                if ((occupant != null) && (occupant.getTeamColor() != teamColor)){
                    Collection<ChessMove> enemyKingThreats = occupant.pieceMoves(boardSub, canKingEnemy);
                    for (ChessMove threat : enemyKingThreats){
                        if (threat.getEndPosition().equals(kingFound)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private ChessBoard copyBoard (ChessBoard original) {
        //make a board
        ChessBoard boardCopy = new ChessBoard();
        //copy over original to the copy
        for (int row = 1; row <= 8; row ++){
            for (int col = 1; col <= 8; col ++){
                ChessPosition curSqr = new ChessPosition(row, col);
                ChessPiece occupant = original.getPiece(curSqr);
                boardCopy.addPiece(curSqr, occupant);
            }
        }
        return boardCopy;
    }

    private ChessPosition findKing (TeamColor teamColor, ChessBoard boardSub){
        for (int row = 1; row <= 8; row ++){
            for (int col = 1; col <= 8; col ++){
                ChessPosition canKing = new ChessPosition(row, col);
                ChessPiece occupant = boardSub.getPiece(canKing);
                if ((occupant != null) && (occupant.getPieceType() == ChessPiece.PieceType.KING) && (occupant.getTeamColor() == teamColor)){
                    return canKing;
                }
            }
        }
        return null;
    }

    private TeamColor teamTurn;
    private ChessBoard board;
    private ChessMove lastMove;
    private HashSet<ChessPosition> playedSquares = new HashSet<>();

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        //Is there a piece
        ChessPiece occupant = board.getPiece(startPosition);
        if (occupant == null) {
            return null;
        }
        //Collect pieces moves
        Collection<ChessMove> possibleMoves = occupant.pieceMoves(board, startPosition);
        //check and add en passant
        if (occupant.getPieceType().equals(ChessPiece.PieceType.PAWN)) {
            ChessMove enPassMove = enPassant(startPosition);
            if (enPassMove != null) {
                possibleMoves.add(enPassMove);
            }
        }
        //check and add castling
        if (occupant.getPieceType().equals(ChessPiece.PieceType.KING)) {
            possibleMoves.addAll(castlingMoves(startPosition));
        }

        //Collect valid moves
        Collection<ChessMove> legalMoves = new ArrayList <> ();
        //for each move
        for (ChessMove move : possibleMoves ) {
            //make a copy board to analyse that move
            ChessBoard testBoard = copyBoard(board);
            removeEnPassantCapture(move, testBoard);
            moveCastlingRook(move, testBoard);
            //start test
            //vacate startPosition
            testBoard.addPiece(move.getStartPosition(), null);
            //occupy endPosition
            testBoard.addPiece(move.getEndPosition(), occupant);
            if (!(isInCheckHelper(occupant.getTeamColor(), testBoard))) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //throw errors
        ChessPiece occupant = board.getPiece(move.getStartPosition());
        if (occupant == null) {
            throw new InvalidMoveException("No piece to move at this position");
        }
        if (occupant.getTeamColor() != teamTurn) {
            throw new InvalidMoveException("Not your turn");
        }
        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("invalid move");
        }
        //en passant
        removeEnPassantCapture(move, board);
        //castling
        moveCastlingRook(move, board);
        //vacate start
        board.addPiece(move.getStartPosition(), null);
        //occupy end (with promoPiece if necessary)
        if (move.getPromotionPiece() != null) {
            ChessPiece promoOccupant = new ChessPiece(occupant.getTeamColor(), move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), promoOccupant);
        }
        else {
            board.addPiece(move.getEndPosition(), occupant);
        }
        //Trade turns
        if (teamTurn == ChessGame.TeamColor.WHITE) {
            setTeamTurn(ChessGame.TeamColor.BLACK);
        }
        else {
            setTeamTurn(ChessGame.TeamColor.WHITE);
        }
        //update trackers
        playedSquares.add(move.getStartPosition());
        playedSquares.add(move.getEndPosition());
        lastMove = move;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheckHelper(teamColor, board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor) && !hasValidMoves(teamColor)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!isInCheck(teamColor) && !hasValidMoves(teamColor)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
