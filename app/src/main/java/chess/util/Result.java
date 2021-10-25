package chess.util;

public class Result<TValue, TCode extends Enum<?>> {
  public final TValue value;
  public final TCode code;

  public Result() {
    this.value = null;
    this.code = null;
  }

  public Result(TValue value) {
    this.value = value;
    this.code = null;
  }

  public Result(TCode code) {
    this.value = null;
    this.code = code;
  }
}
