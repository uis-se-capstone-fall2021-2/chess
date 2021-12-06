package chess.game;

import java.util.Date;
import chess.board.Board;

/**
 * Information for Players during game 
 */
public class GameState extends GameInfo {
  public Board board = null;

  public final long playerInCheck; // playerId, null if no one in check
  public GameState(
    long gameId,
    long owner,
    long winner,
    long[] players,
    long moveCount,
    GameStatus status,
    Date createdAt,
    Date updatedAt,
    Date completedAt,
    long playerInCheck,
    Board board
  ) {
    super(gameId, owner, winner, players, moveCount, status, createdAt, updatedAt, completedAt);
    this.playerInCheck = playerInCheck;
    this.board = board;
  }
}
