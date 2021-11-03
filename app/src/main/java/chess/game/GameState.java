package chess.game;

import chess.board.Board;

/**
 * Information for Players during game 
 */
public class GameState extends GameInfoBase {
  public Board board = null;

  public final long inCheck; // playerId, null if no one in check
  public GameState(
    long gameId,
    long owner,
    long winner,
    long[] players,
    long moveCount,
    long inCheck,
    Board board,
    GameCompletionState completed
  ) {
    super(gameId, owner, winner, players, moveCount, completed);
    this.inCheck = inCheck;
    this.board = board;
  }
}
