package chess.security.ws.messageInterceptors;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

import chess.user.model.IUser;

public class SubscribeInterceptor extends CommandInterceptor {

  public SubscribeInterceptor(StompHeaderAccessor accessor) {
    super(accessor);
  }

  @Override
  public Message<?> handleMessage(Message<?> message, MessageChannel channel) {
    String dest = accessor.getDestination();
      String[] topic = dest.split("/", 0);
      switch(topic[0]) {
        case "users":
          IUser user = (UserPrincipal)accessor.getUser();
          if(topic[1] != user.getUserId()) {
            throw new RuntimeException("Unauthorized");
          }
          break;
        case "players":
          break;
        default:
          throw new RuntimeException("Unknown Topic");
      }

      return message;
  }
  
}
