package chess.security.ws.messageInterceptors;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public class UnsupportedCommandInterceptor extends CommandInterceptor {

  public UnsupportedCommandInterceptor(StompHeaderAccessor accessor) {
    super(accessor);
  }

  @Override
  public Message<?> handleMessage(Message<?> message, MessageChannel channel) {
    throw new RuntimeException(String.format("Unsupported command %s", accessor.getCommand()));
  }
  
}
