package chess.game;

import chess.board.Board;

/**
 * Information for Players during game 
 */
public class GameState extends GameInfoBase {
  public Board board = null;

  public final long playerInCheck; // playerId, null if no one in check
  public GameState(
    long gameId,
    long owner,
    long winner,
    long[] players,
    long moveCount,
    long playerInCheck,
    Board board,
    GameStatus status
  ) {
    super(gameId, owner, winner, players, moveCount, status);
    this.playerInCheck = playerInCheck;
    this.board = board;
  }
}
