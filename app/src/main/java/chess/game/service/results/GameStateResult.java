package chess.game.service.results;

import chess.game.GameState;

public class GameStateResult {
  public static enum Code {
    GAME_NOT_FOUND,
    UNAUTHORIZED,
  }
  public final GameState value;
  public final Code code;

  public GameStateResult(Code code) {
    this.code = code;
    this.value = null;
  }

  public GameStateResult(GameState gameState) {
    this.code = null;
    this.value = gameState;
  }
}
