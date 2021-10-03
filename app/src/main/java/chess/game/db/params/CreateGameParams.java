package chess.game.db.params;

public class CreateGameParams {
  public final long player1;
  public final long player2;
  public final long owner;

  public CreateGameParams(
    long player1,
    long player2,
    long owner
  ) {
    this.player1 = player1;
    this.player2 = player2;
    this.owner = owner;
  }
}
