package chess.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import chess.MoveIntent;
import chess.PlayerColor;
import chess.board.Board;
import chess.game.GameState;
import chess.player.model.Player;

public abstract class ChessAI extends Player {

    static final int MINIMAX_DEPTH = 5;

    public abstract MoveIntent chooseMove(GameState state, List<MoveIntent> moveHistory);

    public PlayerColor team;

    protected ChessAI(PlayerColor team) {
        super();
        this.team = team;
    }


    @Override
    public String getDisplayName() {
        return this.getClass().getSimpleName() + " AI";
    }

    @Override
    public void notify(GameState gameState) {
        //TODO: does notify need to do anything for AI?
    }
    // returns simple piece values
    protected static int getPieceValue(int piece) {
        if(piece == 0) return 0;
        int team = piece / Math.abs(piece);
        switch(Math.abs(piece)) {
            case 0:
            return 0;
            case 1:
            return 10 * team;
            case 2:
            return 32 * team;
            case 3:
            case 4:
            return 33 * team;
            case 5:
            return 91 * team;
            case 6:
            return 900 * team;
            default:
            return 0;

        }
    }
    public static int getBoardScore(Board board) {
        int boardScore = 0;
        for(int i = 0; i < board.board.length; i++){
            int piece = board.board[i];
            boardScore += getPieceValue(piece);
        }
        return boardScore;
    }


}