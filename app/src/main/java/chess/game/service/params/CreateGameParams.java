package chess.game.service.params;

import chess.PlayerColor;

public class CreateGameParams {
  public final long player1;
  public final long player2;
  public final PlayerColor player1Color;

  public CreateGameParams(
    long player1,
    long player2,
    PlayerColor player1color
  ) {
    this.player1 = player1;
    this.player2 = player2;
    this.player1Color = player1color;
  }
}
