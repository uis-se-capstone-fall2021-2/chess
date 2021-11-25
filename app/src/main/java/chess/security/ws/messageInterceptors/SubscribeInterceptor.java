package chess.security.ws.messageInterceptors;

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
    String[] topic = dest.split("/", 0);
    switch(topic[1]) {
      case "games":
        if(topic.length < 3) {
          throw new RuntimeException(String.format("invalid topic '%s'", dest));
        }
        IUser user = (UserPrincipal)accessor.getUser();
        long gameId = Long.parseLong(topic[2]);
        Game game = gameService.getGame(gameId);
        if(game == null) {
          throw new RuntimeException(String.format("no game with id %d", gameId));
        } else if(!game.hasPlayer(user.getPlayerId())) {
          throw new RuntimeException(String.format("%s unauthorized for game id %d", user.getDisplayName(), gameId));
        }
        break;
      case "players":
        break;
      default:
        throw new RuntimeException(String.format("Unsupported topic '%s'", dest));
    }

    return message;
  }
  
}
