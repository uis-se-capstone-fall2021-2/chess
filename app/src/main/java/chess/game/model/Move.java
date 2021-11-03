package chess.game.model;

import java.util.Date;

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
  @Column
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long moveId;

  @Column
  private Date timestamp;

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

  public Move() {}

  public Move(MoveIntent intent) {
    this.chessPiece = intent.chessPiece;
    this.startRank = intent.from.rank;
    this.startFile = intent.from.file;
    this.endRank = intent.to.rank;
    this.endFile = intent.to.file;
  }

  @PrePersist
  public void updateTimeStamp() {
    if(timestamp == null) {
      timestamp = new Date();
    }
  }

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
