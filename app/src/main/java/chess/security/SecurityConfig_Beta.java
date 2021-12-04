package chess.security;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Profile;

@Profile("beta")
@EnableWebSecurity
public class SecurityConfig_Beta extends SecurityConfig_Base {
  
}
