package chess.game.service.params;

public class GetGameStateParams {
  public final long gameId;
  public final long playerId;
  public GetGameStateParams(long gameId, long playerId) {
    this.gameId = gameId;
    this.playerId = playerId;
  }
}
