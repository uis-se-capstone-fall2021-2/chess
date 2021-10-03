package chess.board;

import chess.ChessPiece;
import chess.MoveIntent;
import chess.Position;

public interface IBoard {
  public ChessPiece getPiece(Position position);
  public int[] updateBoard(MoveIntent intent);
}
  