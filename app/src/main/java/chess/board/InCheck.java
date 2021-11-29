package chess.board;

import chess.PlayerColor;

public enum InCheck {
  WHITE(PlayerColor.WHITE.value),
  BLACK(PlayerColor.BLACK.value),
  NONE(0),
  ODD_CASE(2);
  public int value;
  InCheck(int value) {
    this.value = value;
  }
}
