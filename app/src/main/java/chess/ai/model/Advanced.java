package chess.ai.model;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import chess.MoveIntent;
import chess.PlayerColor;
import chess.game.GameState;
import chess.player.model.Player;
/**
 *  Chess AI using a minimax algorithm for move decisions.
 */
@Entity
@DiscriminatorValue(value=Player.PlayerType.AI)
public class Advanced extends ChessAI {

    public Advanced() {
        super();
    }

    @Override
    public MoveIntent chooseMove(GameState state, List<MoveIntent> moveHistory) {
        PlayerColor team = (state.players[0] == getPlayerId()) ? PlayerColor.WHITE : PlayerColor.BLACK;
        BoardTree gameTree = new BoardTree(state.board, moveHistory);
        MoveIntent output = gameTree.findMinimaxMove(team);
        System.out.println("AI has chosen a move: " + output.toString());
        return output;
    }

}
