package chess.game.service.params;

import chess.MoveIntent;

public class UpdateGameParams {
  public final long playerId;
  public final MoveIntent moveIntent;
  public UpdateGameParams(
    long playerId,
    MoveIntent moveIntent
  ) {
    this.playerId = playerId;
    this.moveIntent = moveIntent;
  }
}
