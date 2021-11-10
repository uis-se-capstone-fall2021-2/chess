package chess.ai;

import java.util.List;
import java.util.Random;

import chess.MoveIntent;
import chess.game.GameState;

public class Beginner extends ChessAI {

    @Override
    public MoveIntent chooseMove(GameState state, List<MoveIntent> moveHistory) {

        List<MoveIntent> possibleMoves = this.getAllValidMoves(state, moveHistory);
        // just return a random move...
        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }

}