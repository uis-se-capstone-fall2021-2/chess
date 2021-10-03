package chess.game.service.results;

import chess.game.GameInfo;

public class CreateGameResult {
  public enum Code {
    INVALID_OPPONENT,
    UNKNOWN_OPPONENT
  }

  public final GameInfo value;
  public final Code code;

  public CreateGameResult(GameInfo info) {
    this.value = info;
    this.code = null;
  }

  public CreateGameResult(Code code) {
    this.value = null;
    this.code = code;
  }
}
