package chess;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import chess.board.Board;
/**
 * Tests a variety of moves on the move validator to check for game logic errors
 */
public class MoveValidationTest {
    ArrayList<MoveIntent> moveRecord;
    Board board;

    @BeforeEach
    public void init() {
        moveRecord = new ArrayList<MoveIntent>();
        board = new Board();
        
    }
    @DisplayName("King should not be able to castle when in check.")
    @Test void testKingCastleInCheck() {
        //prepare the board, king is in check.
        String[] moves = {
            "e2 e4",
            "e7 e5",
            "f2 f3",
            "f7 f5",
            "f1 b5",
            "g8 f6",
            "d2 d3",
            "f8 b4",

        };
        for(int i = 0; i < moves.length; i++) {
            MoveIntent curentMove = ChessTestUtilities.stringToMoveIntent(moves[i], board);
            board.updateBoard(curentMove);
            moveRecord.add(curentMove);
        }
        //test king castle attempt (King is in check)
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("e1 g1", board);
        assertFalse(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));

    }
    @DisplayName("King should not be able to castle when in-between square is threatened.")
    @Test void testKingCastleThroughCheck() {
        //prepare the board
        String[] moves = {
            "e2 e4",
            "e7 e5",
            "f2 f3",
            "f7 f6",
            "f1 c4",
            "f8 c5",
            "g1 e2",
            "g8 c7",
        };
        for(int i = 0; i < moves.length; i++) {
            MoveIntent curentMove = ChessTestUtilities.stringToMoveIntent(moves[i], board);
            board.updateBoard(curentMove);
            moveRecord.add(curentMove);
        }
        //test king castle attempt on white side
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("e1 g1", board);
        assertFalse(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));

        //go to black's turn.
        testMove = ChessTestUtilities.stringToMoveIntent("h2 h3", board);
        board.updateBoard(testMove);;
        moveRecord.add(testMove);

        //test on black side
        testMove = ChessTestUtilities.stringToMoveIntent("e8 g8", board);
        assertFalse(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.BLACK));
    }

    @DisplayName("Pawn should not be able to move to the final rank")
    @Test void testPawnNonPromotion() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("h2 h4", board),
            ChessTestUtilities.stringToMoveIntent("g7 g5", board),
            ChessTestUtilities.stringToMoveIntent("h4 g5", board),
            ChessTestUtilities.stringToMoveIntent("h7 h6", board),
            ChessTestUtilities.stringToMoveIntent("g5 h6", board),
            ChessTestUtilities.stringToMoveIntent("e7 e6", board),
            ChessTestUtilities.stringToMoveIntent("h6 h7", board),
            ChessTestUtilities.stringToMoveIntent("e6 e5", board),
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("h7 g8", board);
        assertFalse(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Pawn should be able to promote to queen")
    @Test void testPawnPromotion() {
        //prepare the board
        String[] moves = {
            "h2 h4",
            "g7 g5",
            "h4 g5",
            "h7 h6",
            "g5 h6",
            "e7 e6",
            "h6 h7",
            "e6 e5",
            
        };
        for(int i = 0; i < moves.length; i++) {
            MoveIntent curentMove = ChessTestUtilities.stringToMoveIntent(moves[i], board);
            board.updateBoard(curentMove);
            moveRecord.add(curentMove);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("h7 g8 q", board);
        assertTrue(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Queen should be able to move")
    @Test void testLegalQueenMove() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("e2 e4", board),
            ChessTestUtilities.stringToMoveIntent("e7 e5", board)
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("d1 h5", board);
        assertTrue(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Queen should not be able to move when blocked")
    @Test void testIllegalQueenMove() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("a2 a4", board),
            ChessTestUtilities.stringToMoveIntent("e7 e5", board)
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("d1 h5", board);
        assertFalse(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Queen should not be able to move when it would leave the king in check")
    @Test void testIllegalQueenMoveQueenPinned() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("e2 e4", board),
            ChessTestUtilities.stringToMoveIntent("d7 e5", board),
            ChessTestUtilities.stringToMoveIntent("d1 e2", board),
            ChessTestUtilities.stringToMoveIntent("d8 e7", board),
            ChessTestUtilities.stringToMoveIntent("g1 f3", board),
            ChessTestUtilities.stringToMoveIntent("g6 f6", board),
            ChessTestUtilities.stringToMoveIntent("f3 e5", board),
            ChessTestUtilities.stringToMoveIntent("f6 e4", board),
            ChessTestUtilities.stringToMoveIntent("e5 c4", board),
            ChessTestUtilities.stringToMoveIntent("e4 c5", board),
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("e2 f3", board);
        assertFalse(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Rook should be able to move")
    @Test void testLegalRookMove() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("h2 h4", board),
            ChessTestUtilities.stringToMoveIntent("g7 g5", board),
            ChessTestUtilities.stringToMoveIntent("h4 g5", board),
            ChessTestUtilities.stringToMoveIntent("h7 h5", board),
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
       }
        //test a move one
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("h1 h3", board);
        assertTrue(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Rook should be able to capture")
    @Test void testLegalRookCapture() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("h2 h4", board),
            ChessTestUtilities.stringToMoveIntent("g7 g5", board),
            ChessTestUtilities.stringToMoveIntent("h4 g5", board),
            ChessTestUtilities.stringToMoveIntent("h7 h5", board),
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("h1 h5", board);
        assertTrue(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Rook should not be able to move through a piece")
    @Test void testillegalRookMove() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("h2 h4", board),
            ChessTestUtilities.stringToMoveIntent("g7 g5", board),
            ChessTestUtilities.stringToMoveIntent("h4 g5", board),
            ChessTestUtilities.stringToMoveIntent("h7 h5", board),
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("h1 h6", board);
        assertFalse(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Bishop should be able to move diagonally")
    @Test void testLegalBishopMove() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("e2 e4", board),
            ChessTestUtilities.stringToMoveIntent("e7 e5", board)
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("f1 d3", board);
        assertTrue(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Bishop should be able to capture a piece")
    @Test void testLegalBishopCapture() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("e2 e4", board),
            ChessTestUtilities.stringToMoveIntent("e7 e5", board),
            ChessTestUtilities.stringToMoveIntent("f1 c4", board),
            ChessTestUtilities.stringToMoveIntent("g8 f6", board),
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("c4 f7", board);
        assertTrue(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("King should be able to move")
    @Test void testLegalKingMove() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("e2 e4", board),
            ChessTestUtilities.stringToMoveIntent("e7 e5", board)
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("e1 e2", board);
        assertTrue(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("King should be able to castle")
    @Test void testLegalKingCastle() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("e2 e3", board),
            ChessTestUtilities.stringToMoveIntent("e7 e6", board),
            ChessTestUtilities.stringToMoveIntent("f1 d3", board),
            ChessTestUtilities.stringToMoveIntent("f8 d6", board),
            ChessTestUtilities.stringToMoveIntent("g1 e2", board),
            ChessTestUtilities.stringToMoveIntent("g8 e7", board)
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("e1 g1", board);
        assertTrue(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }
    @DisplayName("Bishop should not be able to move through pieces")
    @Test void testBlockedBishop() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("a2 a3", board),
            ChessTestUtilities.stringToMoveIntent("a7 a6", board)
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("c1 e4", board);
        assertFalse(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }

    @DisplayName("Bishop should be able to capture a piece")
    @Test void testBishopCapturePawn() {
        //prepare the board
        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("e2 e4", board),
            ChessTestUtilities.stringToMoveIntent("b7 b5", board)
            
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
            moveRecord.add(moves[i]);
        }
        //test a move
        MoveIntent testMove = ChessTestUtilities.stringToMoveIntent("f1 b5", board);
        assertTrue(MoveValidator.validateMove(testMove, board, moveRecord, PlayerColor.WHITE));
    }


    // Old version of tests, tests are still good but these are more tedious to set up
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