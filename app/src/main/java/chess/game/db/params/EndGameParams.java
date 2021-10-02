package chess.game.db.params;

import chess.game.GameCompletionState;

public class EndGameParams {
  public final long gameId;
  public final long winner;
  public final GameCompletionState completionState;

  public EndGameParams(
    long gameId,
    long winner,
    GameCompletionState completionState
  ) {
    this.gameId = gameId;
    this.winner = winner;
    this.completionState = completionState;
  }
}
