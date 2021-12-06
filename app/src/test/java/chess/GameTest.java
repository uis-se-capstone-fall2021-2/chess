package chess;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import chess.game.model.Game;

public class GameTest {
    Game game;
    static final long P1 = 0;
    static final long P2 = 1;
    @BeforeEach
    public void init() {
        game = new Game(P1, P2, P1);
    }
    @DisplayName("A pawn moved to the final rank should auto-promote")
    @Test void checkPawnAutoPromoShouldWork() {
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
            MoveIntent currentMove = ChessTestUtilities.stringToMoveIntent(moves[i], game.getBoard());
            assertTrue(game.move(game.currentPlayer(), currentMove), "A game move has failed before the pawn auto-promoted| Move number: " + i);
        }
        MoveIntent promoMove = ChessTestUtilities.stringToMoveIntent("h7 g8", game.getBoard());
        assertTrue(game.move(game.currentPlayer(), promoMove), "Queen auto-promotion move has failed");
    }
    @DisplayName("A pawn moved to the final rank should remain a queen after subsequent turns")
    @Test void checkPawnShouldStayAQueen() {
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
            MoveIntent currentMove = ChessTestUtilities.stringToMoveIntent(moves[i], game.getBoard());
            assertTrue(game.move(game.currentPlayer(), currentMove), "A game move has failed before the pawn auto-promoted| Move number: " + i);
        }
        MoveIntent promoMove = ChessTestUtilities.stringToMoveIntent("h7 g8", game.getBoard());
        assertTrue(game.move(game.currentPlayer(), promoMove), "Queen auto-promotion move has failed");
        moves = new String[] {
            "c7 c5",
            "e2 e4",
            "a7 a5",
            "b2 b4",
            "b7 b5",
        };

        for(int i = 0; i < moves.length; i++){
            MoveIntent currentMove = ChessTestUtilities.stringToMoveIntent(moves[i], game.getBoard());
            assertTrue(game.move(game.currentPlayer(), currentMove), "A game move has failed after the queen promoted| Move number: " + i);
            assertEquals(game.getBoard().getPiece(new Position(File.G, Rank._8)), ChessPiece.QUEEN.value, "Queen is no longer a queen: " + i);
        }
    }
}
