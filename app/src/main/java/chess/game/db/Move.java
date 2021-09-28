package chess.game.db;

import javax.persistence.*;

import chess.ChessPiece;
import chess.File;
import chess.MoveIntent;
import chess.Position;
import chess.Rank;

@Entity
@Table(name="Moves")
public class Move {
  @Id
  private long id;
  @Column
  private ChessPiece chessPiece;
  @Column
  private Rank startRank;
  @Column
  private File startFile;
  @Column
  private Rank endRank;
  @Column
  private File endFile;

  public ChessPiece getChessPiece() {
    return chessPiece;
  }

  public Position getStartPosition() {
    return new Position(startFile, startRank);
  }

  public Position getEndPosition() {
    return new Position(endFile, endRank);
  }

  public MoveIntent asIntent() {
    return new MoveIntent(
      chessPiece,
      new Position(startFile, startRank),
      new Position(endFile, endRank));
  }
}
