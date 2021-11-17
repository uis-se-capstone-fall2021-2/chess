package chess.ai;

import java.util.List;
import java.util.Random;

import chess.MoveIntent;
import chess.MoveValidator;
import chess.PlayerColor;
import chess.game.GameState;

public class Beginner extends ChessAI {

    @Override
    public MoveIntent chooseMove(GameState state, List<MoveIntent> moveHistory, PlayerColor team) {

        List<MoveIntent> possibleMoves = MoveValidator.getAllValidMoves(state, moveHistory, team);
        // just return a random move...
        return possibleMoves.get(new Random().nextInt(possibleMoves.size()));
    }

}