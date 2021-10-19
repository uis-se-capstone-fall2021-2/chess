package chess.security;

import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Profile("development")
@EnableWebSecurity
public class SecurityConfig_Development extends SecurityConfig_Base {

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {

    httpSecurity
      .authorizeRequests()
      .antMatchers(
          "/h2-console/**"
        ).permitAll()
      .and()
      .csrf().disable()
      .headers().frameOptions().disable();

    super.configure(httpSecurity);
  }
}