package game;

/**
 * Information for Players during game 
 */
public class GameState extends GameInfo {

    int inCheck; // playerId, null if no one in check
    public GameState(int gameId, int owner, int winner, int[] players, long moveCount, int inCheck, GameCompletionState completed) {
        super(gameId, owner, winner, players, moveCount, completed);
        this.inCheck = inCheck;
    }


}
