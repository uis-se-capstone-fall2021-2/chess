package chess.user;

import jdk.jfr.Description;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import chess.user.model.User;
import chess.user.service.UserService;

@RestControllerAdvice
public class UserProvider {

  @Autowired
  private UserService userService;
  
  @Description("Injects a User into RestController methods based on Authentication Principal")
  @ModelAttribute
  public User injectUser(@AuthenticationPrincipal Jwt principal) throws Exception {
    if(principal == null) {
      return null;
    }
    return userService.provisionUser(principal);
  }
}
