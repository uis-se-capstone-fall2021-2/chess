package chess.player.model;

import javax.persistence.*;

import chess.game.GameState;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="Players")
@DiscriminatorColumn(name="PLAYER_TYPE", discriminatorType=DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class Player {

  public static class PlayerType {
    public static final String User = "User";
    public static final String AI = "AI";
  }

  public static class Fields {
    public static final String PLAYER_ID = "PLAYER_ID";
  }

  @Id
  @Column(name=Fields.PLAYER_ID)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  protected long playerId;

  @Transient
  public String getPlayerType() {
    return this.getClass().getAnnotation(DiscriminatorValue.class).value();
  }

  public PlayerInfo info() {
    return new PlayerInfo(
      getPlayerId(),
      getDisplayName(),
      getPlayerType()
    );
  }

  public abstract String getDisplayName();
  public abstract void notify(GameState gameState);
}
