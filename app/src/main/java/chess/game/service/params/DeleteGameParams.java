package chess.game.service.params;

public class DeleteGameParams {
  public final long gameId;
  public final long playerId;
  public DeleteGameParams(long gameId, long playerId) {
    this.gameId = gameId;
    this.playerId = playerId;
  }
}
