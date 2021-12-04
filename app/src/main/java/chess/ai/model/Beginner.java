package chess.ai.model;

import java.util.List;
import java.util.Random;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import chess.MoveIntent;
import chess.MoveValidator;
import chess.PlayerColor;
import chess.game.GameState;

/**
 *  Chess AI using a random number generator to make move decisions.
 */
@Entity
@DiscriminatorValue(value=ChessAI.AIType.Beginner)
public class Beginner extends ChessAI {
    @Transient
    private final Random r;
    public Beginner() {
        super();
        r = new Random();
    }
    @Override
    public MoveIntent chooseMove(GameState state, List<MoveIntent> moveHistory) {
        PlayerColor team = (state.players[0] == getPlayerId()) ? PlayerColor.WHITE : PlayerColor.BLACK;
        List<MoveIntent> possibleMoves = MoveValidator.getAllValidMoves(state, moveHistory, team);
        // just return a random move...
        return possibleMoves.get(r.nextInt(possibleMoves.size()));
    }

}