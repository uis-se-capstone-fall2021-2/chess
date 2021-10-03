package chess.game;

/**
 * A lightweight summary of a Game for display in a list view
 */
public class GameInfo {

    public final long gameId;
    public final long owner; // playerId
    public final long winner; // playerId
    public final long[] players; // playerId, array of length 2, first player is white
    public final long moveCount;
    public final GameCompletionState state;

    // default constructor
    public GameInfo(long gameId, long owner, long winner, long[] players, long moveCount, GameCompletionState completionState) {
        this.gameId = gameId;
        this.owner = owner;
        this.winner = winner;
        this.players = players;
        this.moveCount = moveCount;
        this.state = completionState;
    }
}
