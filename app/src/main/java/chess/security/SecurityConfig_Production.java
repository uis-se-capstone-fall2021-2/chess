package chess.security;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Profile;

@Profile("production")
@EnableWebSecurity
public class SecurityConfig_Production extends SecurityConfig_Base {
  
}
