package chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class MoveValidationTest {
    MoveValidator validator;
    ArrayList<MoveIntent> moveRecord;
    int[] board;

    @BeforeEach
    public void init() {
        validator = new MoveValidator();
        moveRecord = new ArrayList<MoveIntent>();
        board = new int[] {
            2,3,4,6,5,4,3,2,
            1,1,1,1,1,1,1,1,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,
            -1,-1,-1,-1,-1,-1,-1,-1,
            -2,-3,-4,-6,-5,-4,-3,-2
        };
        
    }

    @DisplayName("Knight should be able to move to open space")
    @Test void testLegalKnightMove() {
        Position currentPosition = new Position(File.FromInteger(1), Rank.FromInteger(0)); // knight at B1
        Position desiredPosition = new Position(File.FromInteger(2), Rank.FromInteger(2)); // Knight at C3
        ChessPiece piece = ChessPiece.FromInteger(Math.abs(validator.getPiece(currentPosition,board)));
        assertTrue(validator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Knight should be able to jump on opponent")
    @Test void testLegalKnightCapture() {
        Position currentPosition = new Position(File.FromInteger(1), Rank.FromInteger(0)); // knight at B1
        Position desiredPosition = new Position(File.FromInteger(2), Rank.FromInteger(2)); // Knight at C3
        board[18] = -1; // put an enemy pawn blocking the desired spot, C3
        ChessPiece piece = ChessPiece.FromInteger(Math.abs(validator.getPiece(currentPosition,board)));
        assertTrue(validator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Knight should not be able to jump on it's own teamate")
    @Test void testIllegalKnightMove() {
        Position currentPosition = new Position(File.FromInteger(1), Rank.FromInteger(0)); // knight at B1
        Position desiredPosition = new Position(File.FromInteger(2), Rank.FromInteger(2)); // Knight at C3
        board[18] = 1; // put a pawn blocking the desired spot, C3
        ChessPiece piece = ChessPiece.FromInteger(Math.abs(validator.getPiece(currentPosition,board)));
        assertFalse(validator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Knight should not be able to jump out of bounds")
    @Test void testOutOfBoundsKnightMove() {
        board[16] = 3; // Knight at A3
        Position currentPosition = new Position(File.FromInteger(0), Rank.FromInteger(2)); // knight at B1
        Position desiredPosition = new Position(File.FromInteger(-1), Rank.FromInteger(4)); // Knight at C3
        ChessPiece piece = ChessPiece.FromInteger(Math.abs(validator.getPiece(currentPosition,board)));
        assertFalse(validator.validateMove(new MoveIntent(piece, currentPosition, desiredPosition), board, moveRecord, PlayerColor.WHITE));
    }
}
