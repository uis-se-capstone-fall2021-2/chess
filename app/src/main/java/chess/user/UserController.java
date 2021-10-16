package chess.user;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins={"*"})
public class UserController {
  
  @GetMapping("/user/whoami")
  public String getUser(Authentication authentication) {
    Jwt token = (Jwt)authentication.getPrincipal();
    return token.getSubject();
  }
}
