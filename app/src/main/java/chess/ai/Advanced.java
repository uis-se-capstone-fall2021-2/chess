package chess.ai;
import java.util.List;

import chess.MoveIntent;
import chess.PlayerColor;
import chess.game.GameState;

public class Advanced extends ChessAI {

    public Advanced(PlayerColor team) {
        super(team);
    }

    @Override
    public MoveIntent chooseMove(GameState state, List<MoveIntent> moveHistory) {
        // TODO Auto-generated method stub
        BoardTree gameTree = new BoardTree(state.board, moveHistory);
        MoveIntent output = gameTree.findMinimaxMove();
        System.out.println("AI has chosen a move after checking " + gameTree.treeSize + " possibilities");
        return output;
    }
    
}