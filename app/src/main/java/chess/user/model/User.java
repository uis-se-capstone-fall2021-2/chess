package chess.user.model;

import javax.persistence.*;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

import chess.game.GameState;
import chess.notifications.service.NotificationService;
import chess.player.model.Player;
import chess.MoveIntent;


@Entity
@DiscriminatorValue(value=Player.PlayerType.User)
@Table(name="Users")
@NoArgsConstructor
public class User extends Player implements IUser {
  public static class Fields {
    public static final String USER_ID = "USER_ID";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_DISPLAY_NAME = "USER_DISPLAY_NAME";
  }

  public User(String userId, String email) {
    this.userId = userId;
    this.email = email;
  }

  @Column(name=Fields.USER_ID, unique=true)
  @Getter
  private String userId;

  @Column(name=Fields.USER_EMAIL)
  @Getter
  private String email;

  @Override
  public String getPlayerType() {
    return Player.PlayerType.User;
  }

  public void notify(GameState gameState, List<MoveIntent> moveHistory) {
    if(springApplicationContext == null) {
      System.out.println(String.format("Application not set for for user entity %s", getDisplayName()));
      return;
    }
    NotificationService notificationService = springApplicationContext.getBean(NotificationService.class);
    notificationService.sendGameUpdateNotificationToUser(gameState.gameId, getUserId());
  }
}
