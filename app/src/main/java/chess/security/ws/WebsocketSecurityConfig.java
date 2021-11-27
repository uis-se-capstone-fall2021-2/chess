package chess.security.ws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebsocketSecurityConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${webclient.url}")
  private String webclient;
  @Autowired
  private StompInboundCommandInterceptor inboundCommandInterceptor;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {

    // Set prefix for the endpoint that the client listens for our messages from
    // registry.enableSimpleBroker("/games");

    // Set prefix for endpoints the client will send messages to
    // registry.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // Registers the endpoint where the connection will take place
    registry.addEndpoint("/stomp")
      .setAllowedOrigins(webclient);
      // Enable SockJS fallback options
      //.withSockJS();
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(inboundCommandInterceptor);
	}

}

