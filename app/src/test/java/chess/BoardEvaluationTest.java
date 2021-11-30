package chess;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import chess.ai.model.ChessAI;
import chess.board.Board;

public class BoardEvaluationTest {
    Board board;

    @BeforeEach
    public void init() {
        board = new Board();
        
    }

    @DisplayName("A standard board should evaluate as zero")
    @Test void checkDefaultEvaluation() {
        int boardScore = ChessAI.getBoardScore(board);
        assertEquals(0, boardScore);
    }

    @DisplayName("A board with mirrored moves should evaluate as zero")
    @Test void checkEqualEvaluation() {

        MoveIntent[] moves = {
            ChessTestUtilities.stringToMoveIntent("e2 e4", board),
            ChessTestUtilities.stringToMoveIntent("e7 e5", board),
            ChessTestUtilities.stringToMoveIntent("f1 c3", board),
            ChessTestUtilities.stringToMoveIntent("f8 c6", board),
        };
        for(int i = 0; i < moves.length; i++) {
            board.updateBoard(moves[i]);
        }
        int boardScore = ChessAI.getBoardScore(board);
        assertEquals(0, boardScore);
    }
}
