package chess.game;

/**
 * Information for Players during game 
 */
public class GameState extends GameInfo {

  long inCheck; // playerId, null if no one in check
  public GameState(long gameId, long owner, long winner, long[] players, long moveCount, long inCheck, GameCompletionState completed) {
    super(gameId, owner, winner, players, moveCount, completed);
    this.inCheck = inCheck;
  }
}
