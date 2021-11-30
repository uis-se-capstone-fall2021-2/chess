package chess.user.model;

import javax.persistence.*;

import chess.game.GameState;
import chess.player.model.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@DiscriminatorValue(value=Player.PlayerType.User)
@Table(name="Users")
@AllArgsConstructor
@NoArgsConstructor
public class User extends Player {
  public static class Fields {
    public static final String USER_ID = "USER_ID";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_DISPLAY_NAME = "USER_DISPLAY_NAME";
  }

  @Column(name=Fields.USER_ID, unique=true)
  @Getter
  private String userId;

  @Column(name=Fields.USER_EMAIL)
  @Getter
  private String email;

  public void notify(GameState gameState) {
    return;
  }
}
