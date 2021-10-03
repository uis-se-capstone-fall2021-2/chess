package chess.game.controller.requests;

import chess.PlayerColor;

public class CreateGameRequest {
  public final long opponentId;
  public final PlayerColor playerColor;

  public CreateGameRequest(
    PlayerColor playerColor,
    long opponentId
  ) {
    this.opponentId = opponentId;
    this.playerColor = playerColor;
  }
}

