package chess.game;

/**
 * Information for Players during game 
 */
public class GameState extends GameInfo {

  int inCheck; // playerId, null if no one in check
  public GameState(long gameId, long owner, long winner, long[] players, long moveCount, int inCheck, GameCompletionState completed) {
    super(gameId, owner, winner, players, moveCount, completed);
    this.inCheck = inCheck;
      }

  public GameState(long gameId, long owner, long winner, long[] players, long moveCount, GameCompletionState completed) {
    super(gameId, owner, winner, players, moveCount, completed);
  }


}
