package chess;
/**
 * A lightweight summary of a Game for display in a list view
 */
public abstract class GameInfo {
    int gameId;
    int owner;
    int winner;
    int[] players; // array of length 2
    long moveCount;
    GameCompletionState completed;

    // default constructor
    public GameInfo(int gameId, int owner, int winner, int[] players, long moveCount, GameCompletionState completed) {
        this.gameId = gameId;
        this.owner = owner;
        this.winner = winner;
        this.players = players;
        this.moveCount = moveCount;
        this.completed = completed;
    }
}
