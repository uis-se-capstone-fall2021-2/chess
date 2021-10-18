package chess.player.model;

import javax.persistence.*;

import chess.game.GameState;


@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="Players")
@DiscriminatorColumn(name="PLAYER_TYPE")
public abstract class Player {

  public static enum PlayerType {
    User,
    AI
  }

  @Id
  @Column(name="PLAYER_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long playerId;

  public Player() {}

  public long getPlayerId() {
    return playerId;
  }

  public abstract String getDisplayName();
  public abstract void notify(GameState gameState);
}
