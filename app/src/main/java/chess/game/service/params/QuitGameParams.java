package chess.game.service.params;

public class QuitGameParams {
  public final long gameId;
  public final long playerId;

  public QuitGameParams(
    long _gameId,
    long _playerId
  ) {
    gameId = _gameId;
    playerId = _playerId;
  }
}
