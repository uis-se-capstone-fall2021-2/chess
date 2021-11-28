package chess.security.ws.messageInterceptors;

import java.util.Arrays;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import lombok.AllArgsConstructor;
import chess.game.model.Game;
import chess.game.service.IGameService;
import chess.user.model.IUser;


@AllArgsConstructor
public class SubscribeInterceptor extends CommandInterceptor {
  private final IGameService gameService;

  @Override
  public Message<?> handleMessage(Message<?> message, MessageChannel channel) {
    final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    String dest = accessor.getDestination();
    List<String> topic = Arrays.asList(dest.split("/", 0));
    IUser user = (UserPrincipal)accessor.getUser();
    switch(topic.get(1)) {
      case "users":
        String userId = topic.get(2);
        if(!user.getUserId().equals(userId)) {
          throw new RuntimeException("Unauthorized");
        }
        switch(topic.get(3)) {
          case "games":
            long gameId = Long.parseLong(topic.get(4));
            authorizeGameSubscription(user, gameId);
            break;
          default:
            throw new RuntimeException(String.format("Unsupported topic '%s'", dest));
        }
        break;
      case "games":
        long gameId = Long.parseLong(topic.get(2));
        authorizeGameSubscription(user, gameId);
        break;
      case "players":
        break;
      default:
        throw new RuntimeException(String.format("Unsupported topic '%s'", dest));
    }

    return message;
  }

  private void authorizeGameSubscription(IUser user, long gameId) {
    Game game = gameService.getGame(gameId);
    if(game == null) {
      throw new RuntimeException(String.format("no game with id %d", gameId));
    } else if(!game.hasPlayer(user.getPlayerId())) {
      throw new RuntimeException(String.format("%s unauthorized for game id %d", user.getDisplayName(), gameId));
    }
  }
  
}
