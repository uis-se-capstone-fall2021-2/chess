package chess.security.ws.messageInterceptors;

import java.security.Principal;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import lombok.AllArgsConstructor;

import chess.game.GameState;
import chess.player.model.PlayerInfo;
import chess.user.model.IUser;

@AllArgsConstructor
public abstract class CommandInterceptor {
  protected final StompHeaderAccessor accessor;

  public abstract Message<?> handleMessage(Message<?> message, MessageChannel channel);

  @AllArgsConstructor
  protected class UserPrincipal implements Principal, IUser {
    private final IUser user;

    @Override
    public String getName() {
      return user.getUserId();
    }

    @Override
    public long getPlayerId() {
      return user.getPlayerId();
    }

    @Override
    public String getDisplayName() {
      return user.getDisplayName();
    }

    @Override
    public String getPlayerType() {
      return user.getPlayerType();
    }

    @Override
    public PlayerInfo info() {
      return user.info();
    }

    @Override
    public void notify(GameState gamestate) {
      user.notify(gamestate);
    }

    @Override
    public String getUserId() {
      return user.getUserId();
    }

    @Override
    public String getEmail() {
      return user.getEmail();
    }
  }
}
