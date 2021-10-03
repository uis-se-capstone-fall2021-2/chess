package chess.game.service.params;

import chess.PlayerColor;

public class CreateGameParams {
  public final long playerId;
  public final long opponentId;
  public final PlayerColor playerColor;

  public CreateGameParams(
    long playerId,
    PlayerColor playerColor,
    long opponentId
  ) {
    this.playerId = playerId;
    this.playerColor = playerColor;
    this.opponentId = opponentId;
  }
}
