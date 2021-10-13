package chess.board;

import chess.MoveIntent;
import chess.Position;

public interface IBoard {
  public int getPiece(Position position);
  public int[] updateBoard(MoveIntent intent, int playerNumber);
}
  