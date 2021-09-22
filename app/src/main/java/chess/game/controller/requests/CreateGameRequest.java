package chess.game.controller.requests;

import chess.PlayerColor;

public class CreateGameRequest {
  public final long opponentId;
  public final PlayerColor player1Color;

  public CreateGameRequest(
    PlayerColor _player1color,
    long _opponentId
  ) {
    opponentId = _opponentId;
    player1Color = _player1color;
  }
}

