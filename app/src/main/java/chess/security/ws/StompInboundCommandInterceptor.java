package chess.security.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import chess.security.ws.messageInterceptors.CommandInterceptor;
import chess.security.ws.messageInterceptors.ConnectInterceptor;
import chess.security.ws.messageInterceptors.SubscribeInterceptor;
import chess.security.ws.messageInterceptors.UnsupportedCommandInterceptor;
import chess.user.service.UserService;


@Service
@AllArgsConstructor
public class StompInboundCommandInterceptor implements ChannelInterceptor {
  @Autowired
  private final JwtDecoder jwtDecoder;
  @Autowired
  private final UserService userService;
  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    if(accessor == null) {
      return null;
    }

    StompCommand command = accessor.getCommand();
    CommandInterceptor interceptor;
    switch(command) {
      case CONNECT:
        interceptor = new ConnectInterceptor(accessor, jwtDecoder, userService);
      case SUBSCRIBE:
        interceptor = new SubscribeInterceptor(accessor);
      case UNSUBSCRIBE:
      case DISCONNECT:
      case ACK:
      case NACK:
        return message;
      default:
        interceptor = new UnsupportedCommandInterceptor(accessor);
    }

    return interceptor.handleMessage(message, channel);
  }

  
}

