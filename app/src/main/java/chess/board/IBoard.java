package chess.board;

import chess.MoveIntent;
import chess.Position;

public interface IBoard {
  /** Get which piece is located at a requested location on the board.
   * @param position requested position input
   * @return int integer representation of the requested piece
   */
  public int getPiece(Position position);
  /** Executes a supplied MoveIntent on the board. The MoveIntent is assumed to be valid.
   *  Makes the correct changes to the board that need to be made when a king castles, pawn is taken through en passant rule, and pieces are promoted.
   * @param intent MoveIntent object representing desired move.
   * @return int[] the updated board (can be ignored, the board is updated in place)
   */
  public int[] updateBoard(MoveIntent intent);
}
  