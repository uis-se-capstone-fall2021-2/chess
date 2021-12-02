package chess.board;

import chess.PlayerColor;
/**
 * Enum representing which team is in check, or none.
 * Has an extra case for both players being in check, this is not possible in a real game but is used in move validation.
 */
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
