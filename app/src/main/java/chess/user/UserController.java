package chess.user;

import org.springframework.http.MediaType;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins={"*"})
@SecurityRequirement(name="chess-api")
public class UserController {
  
  @GetMapping("/user/whoami")
  public String getUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Jwt token = (Jwt)auth.getPrincipal();
    return token.getSubject();
  }
}
