package chess;

import java.util.ArrayList;

/**
 * How a game is persisted to db
 */
public class GameRecord extends GameInfo{
    //movesRecord should hold verified MoveIntents in the order that the moves were made. index 0: White move 1, index 1: black move 1, index 2: white move 2...
    ArrayList<MoveIntent> movesRecord;

    public GameRecord(int gameId, int owner, int winner, int[] players, long moveCount, GameCompletionState completed, ArrayList<MoveIntent> movesRecord) {
        super(gameId, owner, winner, players, moveCount, completed);
        this.movesRecord = movesRecord;
    }
}
