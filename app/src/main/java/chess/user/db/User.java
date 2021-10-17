package chess.user.db;

import javax.persistence.*;

import chess.game.GameState;
import chess.player.db.Player;

@Entity
@DiscriminatorValue("User")
@Table(
  name="Users",
  uniqueConstraints=@UniqueConstraint(columnNames="USER_ID")
)
public class User extends Player {
  @Column(name="USER_ID")
  private String userId;

  @Column(name="USER_EMAIL")
  private String email;

  @Column(name="USER_DISPLAY_NAME")
  private String displayName;

  public User() {}

  public User(String userId, String email, String displayName) {
    this.userId = userId;
    this.email = email;
    this.displayName = displayName;
  }

  public String getUserId() {
    return userId;
  }

  public String getEmail() {
    return email;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void notify(GameState gameState) {
    return;
  }
}
