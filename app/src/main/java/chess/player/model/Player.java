package chess.player.model;

import javax.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import chess.game.GameState;
import chess.util.persistence.ContextAwareEntity;
import chess.MoveIntent;


/**
 * Representation of a player
 */
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name="Players")
@DiscriminatorColumn(name="PLAYER_TYPE", discriminatorType=DiscriminatorType.STRING)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Player extends ContextAwareEntity implements IPlayer {

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
  protected long playerId;

  @Column(name=Fields.PLAYER_DISPLAY_NAME)
  @Getter
  @Setter
  private String displayName;
  
  public abstract String getPlayerType();

  public PlayerInfo info() {
    return new PlayerInfo(
      getPlayerId(),
      getDisplayName(),
      getPlayerType()
    );
  }

  public abstract void notify(GameState gameState, List<MoveIntent> moveHistory);
  
}


