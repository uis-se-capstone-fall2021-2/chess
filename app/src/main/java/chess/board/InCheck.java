package chess.board;

import chess.PlayerColor;

public enum InCheck {
  WHITE(PlayerColor.WHITE.value),
  BLACK(PlayerColor.BLACK.value),
  NONE(0),
  BOTH(2);
  public int value;
  InCheck(int value) {
    this.value = value;
  }
}
