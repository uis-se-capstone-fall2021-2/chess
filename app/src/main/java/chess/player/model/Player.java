package chess.player.model;

import javax.persistence.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import chess.game.GameState;
import chess.notifications.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;


@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="Players")
@DiscriminatorColumn(name="PLAYER_TYPE", discriminatorType=DiscriminatorType.STRING)
@NoArgsConstructor
@AllArgsConstructor
@Configurable
public abstract class Player implements IPlayer {

  public static class PlayerType {
    public static final String User = "User";
    public static final String AI = "AI";
  }

  public static class Fields {
    public static final String PLAYER_ID = "PLAYER_ID";
    public static final String PLAYER_DISPLAY_NAME = "PLAYER_DISPLAY_NAME";
  }

  @Id
  @Column(name=Fields.PLAYER_ID)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Getter
  private long playerId;

  @Column(name=Fields.PLAYER_DISPLAY_NAME)
  @Getter
  @Setter
  private String displayName;

  public String getPlayerType() {
    return this.getClass().getAnnotation(DiscriminatorValue.class).value();
  }

  @Transient
  @Autowired
  protected NotificationService notificationService;

  public PlayerInfo info() {
    return new PlayerInfo(
      getPlayerId(),
      getDisplayName(),
      getPlayerType()
    );
  }

  public abstract void notify(GameState gameState);
}
