package game;
/**
 * How a game is persisted to db
 */
public class GameRecord extends GameInfo{
    int[] board;
    // TODO: add record of algebraic moves here
    public GameRecord(int gameId, int owner, int winner, int[] players, long moveCount, GameCompletionState completed, int[] board) {
        super(gameId, owner, winner, players, moveCount, completed);
        this.board = board;
    }
}
