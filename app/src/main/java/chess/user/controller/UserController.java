package chess.user.controller;

import java.util.Map;
import java.util.HashMap;

import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chess.user.controller.requests.UpdateDisplayNameRequest;
import chess.user.db.User;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins={"*"})
@SecurityRequirement(name="chess-api")
public class UserController {
  
  @GetMapping("/user")
  public Map<String, Object> getUserInfo(@Parameter(hidden=true) User user) {
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("userId", user.getUserId());
    map.put("playerId", user.getPlayerId());
    map.put("displayName", user.getDisplayName());
    map.put("email", user.getEmail());
    return map;
  }

  @PatchMapping("/user")
  public void updateDisplayName(
    @Parameter(hidden=true) User user,
    @RequestBody(required=true) UpdateDisplayNameRequest req
  ) {
    user.updateDisplayName(req.displayName);
  }
}
