package chess.security.ws.messageInterceptors;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;

public class UnsupportedCommandInterceptor extends CommandInterceptor {

  @Override
  public Message<?> handleMessage(Message<?> message, MessageChannel channel) {
    final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    throw new RuntimeException(String.format("Unsupported command %s", accessor.getCommand()));
  }
  
}
