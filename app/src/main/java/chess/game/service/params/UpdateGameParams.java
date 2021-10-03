package chess.game.service.params;

import chess.MoveIntent;

public class UpdateGameParams {
  public final long gameId;
  public final long playerId;
  public final MoveIntent moveIntent;
  public UpdateGameParams(
    long gameId,
    long playerId,
    MoveIntent moveIntent
  ) {
    this.gameId = gameId;
    this.playerId = playerId;
    this.moveIntent = moveIntent;
  }
}
