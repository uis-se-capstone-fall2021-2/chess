package chess.security.ws.messageInterceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import chess.user.model.IUser;
import chess.user.service.UserService;

public class ConnectInterceptor extends CommandInterceptor {
  private static final String TOKEN_HEADER = "authToken";

  @Autowired
  private final JwtDecoder jwtDecoder;
  @Autowired
  private final UserService userService;
  
  public ConnectInterceptor(
    StompHeaderAccessor accessor,
    JwtDecoder jwtDecoder,
    UserService userService
  ) {
    super(accessor);
    this.jwtDecoder = jwtDecoder;
    this.userService = userService;
  }

  public Message<?> handleMessage(Message<?> message, MessageChannel channel) {
    final String token = accessor.getFirstNativeHeader(TOKEN_HEADER);
    Jwt principal = jwtDecoder.decode(token);
    if(principal == null) {
      throw new RuntimeException("Authentication failed");
    }

    IUser user;
    try {
      user = userService.provisionUser(principal);
    } catch(Exception e) {
      throw new RuntimeException("Unable to authenticate");
    }
    

    if(user == null) {
      throw new RuntimeException("Authentication failed");
    } else {
      accessor.setUser(new UserPrincipal(user));
    }

    return message;
  }
}
