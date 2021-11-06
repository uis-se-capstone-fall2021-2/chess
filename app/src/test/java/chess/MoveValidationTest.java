package chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import chess.board.Board;

public class MoveValidationTest {
    MoveValidator validator;
    ArrayList<MoveIntent> moveRecord;
    Board board;

    @BeforeEach
    public void init() {
        moveRecord = new ArrayList<MoveIntent>();
        board = new Board();
        
    }

    @DisplayName("Knight should be able to move to open space")
    @Test void testLegalKnightMove() {
        Position currentPosition = new Position(File.FromInteger(1), Rank.FromInteger(0)); // knight at B1
        Position desiredPosition = new Position(File.FromInteger(2), Rank.FromInteger(2)); // Knight at C3
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertTrue(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Knight should be able to jump on opponent")
    @Test void testLegalKnightCapture() {
        Position currentPosition = new Position(File.FromInteger(1), Rank.FromInteger(0)); // knight at B1
        Position desiredPosition = new Position(File.FromInteger(2), Rank.FromInteger(2)); // Knight at C3
        board.board[18] = -1; // put an enemy pawn blocking the desired spot, C3
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertTrue(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Knight should not be able to jump on it's own teamate")
    @Test void testIllegalKnightMove() {
        Position currentPosition = new Position(File.FromInteger(1), Rank.FromInteger(0)); // knight at B1
        Position desiredPosition = new Position(File.FromInteger(2), Rank.FromInteger(2)); // Knight at C3
        board.board[18] = 1; // put a pawn blocking the desired spot, C3
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertFalse(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Knight should not be able to jump out of bounds")
    @Test void testOutOfBoundsKnightMove() {
        board.board[16] = 3; // Knight at A3
        Position currentPosition = new Position(File.FromInteger(0), Rank.FromInteger(2)); // knight at B1
        Position desiredPosition = new Position(File.FromInteger(-1), Rank.FromInteger(4)); // Knight at C3
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertFalse(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Pawn should be able to move forward")
    @Test void testPawnMovement() {
        Position currentPosition = new Position(File.FromInteger(2), Rank.FromInteger(1)); // pawn at C2
        Position desiredPosition = new Position(File.FromInteger(2), Rank.FromInteger(2)); // pawn at C3
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertTrue(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Pawn should be able to move forward twice on first move")
    @Test void testPawnDoubleMovement() {
        Position currentPosition = new Position(File.FromInteger(2), Rank.FromInteger(1)); // pawn at C2
        Position desiredPosition = new Position(File.FromInteger(2), Rank.FromInteger(3)); // pawn at C4
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertTrue(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Pawn should not be able to double move if not on starting position")
    @Test void testPawnDoubleMovementIllegal() {
        board.board[18] = 1;
        Position currentPosition = new Position(File.FromInteger(2), Rank.FromInteger(2)); // pawn at C3
        Position desiredPosition = new Position(File.FromInteger(2), Rank.FromInteger(4)); // pawn at C5
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertFalse(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Pawn should not be able to move diagonally without capturing")
    @Test void testPawnImproperMove() {
        Position currentPosition = new Position(File.FromInteger(2), Rank.FromInteger(1)); // pawn at C2
        Position desiredPosition = new Position(File.FromInteger(3), Rank.FromInteger(2)); // pawn at D3
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertFalse(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Pawn should be able to capture piece")
    @Test void testPawnCapture() {
        board.board[19] = -1;
        Position currentPosition = new Position(File.FromInteger(2), Rank.FromInteger(1)); // pawn at C2
        Position desiredPosition = new Position(File.FromInteger(3), Rank.FromInteger(2)); // pawn at D3
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertTrue(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Pawn should be able to en passant capture")
    @Test void testPawnCaptureEnPassant() {
        board.board[10] = 0;
        board.board[34] = 1;
        board.board[35] = -1;
        Position prev = new Position(File.FromInteger(3), Rank.FromInteger(6));
        Position cur = new Position(File.FromInteger(3), Rank.FromInteger(4));
        moveRecord.add(new MoveIntent(ChessPiece.PAWN, prev, cur));
        Position currentPosition = new Position(File.FromInteger(2), Rank.FromInteger(4)); // pawn at C5
        Position desiredPosition = new Position(File.FromInteger(3), Rank.FromInteger(5)); // pawn at D6
        ChessPiece piece = ChessPiece.FromInteger(board.getPiece(currentPosition));
        assertTrue(MoveValidator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
}
