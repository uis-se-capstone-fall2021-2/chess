package chess.user.model;

import javax.persistence.*;

import chess.game.GameState;
import chess.player.model.Player;

@Entity
@DiscriminatorValue("User")
@Table(name="Users")
public class User extends Player {
  public static class Fields {
    public static final String USER_ID = "USER_ID";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_DISPLAY_NAME = "USER_DISPLAY_NAME";
  }

  @Column(name=Fields.USER_ID, unique=true)
  private String userId;

  @Column(name=Fields.USER_EMAIL)
  private String email;

  @Column(name=Fields.USER_DISPLAY_NAME, unique=true)
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

  public void updateDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void notify(GameState gameState) {
    return;
  }
}
