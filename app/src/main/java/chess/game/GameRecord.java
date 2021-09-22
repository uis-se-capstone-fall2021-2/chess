package chess.game;

import java.util.ArrayList;

import chess.MoveIntent;

/**
 * How a game is persisted to db
 */
public class GameRecord extends GameInfo{
    //movesRecord should hold verified MoveIntents in the order that the moves were made. index 0: White move 1, index 1: black move 1, index 2: white move 2...
    ArrayList<MoveIntent> movesRecord;

    public GameRecord(long gameId, long owner, long winner, long[] players, long moveCount, GameCompletionState completed, ArrayList<MoveIntent> movesRecord) {
        super(gameId, owner, winner, players, moveCount, completed);
        this.movesRecord = movesRecord;
    }
}
