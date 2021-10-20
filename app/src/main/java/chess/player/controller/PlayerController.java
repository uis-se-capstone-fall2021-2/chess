package chess.player.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.AllArgsConstructor;

import chess.player.model.PlayerInfo;
import chess.player.service.PlayerService;
import chess.player.service.errorCodes.GetPlayerInfoErrorCode;
import chess.util.Result;



@RestController
@RequestMapping(path = "/api/v1/players", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins={"*"})
@SecurityRequirement(name="chess-api")
@AllArgsConstructor
public class PlayerController {

  @Autowired
  private final PlayerService playerService;

  @GetMapping("/{id}")
  public PlayerInfo getPlayerInfo(@PathVariable(value="id", required=true) long playerId) {
    Result<PlayerInfo, GetPlayerInfoErrorCode> result = playerService.getPlayerInfo(playerId);
    if(result.code != null) {
      switch(result.code) {
        case NOT_FOUND:
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        default:
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      return result.value;
    }
  }

  // @GetMapping("{id}/games")
  // public List<GameInfo> getGameHistory(@PathVariable(value="id", required=true) long playerId) {

  // }
}
