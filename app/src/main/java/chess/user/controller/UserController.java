package chess.user.controller;

import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import chess.user.controller.requests.UpdateDisplayNameRequest;
import chess.user.model.User;
import chess.user.service.UserService;

@RestController
@RequestMapping(path = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name="chess-api")
public class UserController {

  @Autowired
  private UserService userService;
  
  @GetMapping()
  public Map<String, Object> getUserInfo(@Parameter(hidden=true) User user) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("userId", user.getUserId());
    map.put("playerId", user.getPlayerId());
    map.put("displayName", user.getDisplayName());
    map.put("email", user.getEmail());
    return map;
  }

  @PatchMapping("/")
  public void updateDisplayName(
    @Parameter(hidden=true) User user,
    @RequestBody(required=true) UpdateDisplayNameRequest req
  ) {
    String displayName = req.displayName;
    if(userService.getUserByDisplayName(displayName) != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already in use");
    }
    user.setDisplayName(req.displayName);
  }
}
