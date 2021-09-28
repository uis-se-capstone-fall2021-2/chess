package chess.game.service.results;

import chess.game.GameState;

public class UpdateGameResult {
  public enum Code {
    GAME_NOT_FOUND,
    UNAUTHORIZED,
    ILLEGAL_MOVE,
    OUT_OF_TURN,
  }

  public final GameState value;
  public final Code code;

  public UpdateGameResult(GameState gameState) {
    this.value = gameState;
    this.code = null;
  }

  public UpdateGameResult(Code code) {
    this.value = null;
    this.code = code;
  }
}
