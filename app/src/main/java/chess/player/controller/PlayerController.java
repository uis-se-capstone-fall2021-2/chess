package chess.player.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.AllArgsConstructor;
import chess.game.GameInfo;
import chess.game.GameStatus;
import chess.player.model.PlayerInfo;
import chess.player.service.PlayerService;
import chess.player.service.errorCodes.GetPlayerInfoErrorCode;
import chess.player.service.errorCodes.ListGamesErrorCode;
import chess.player.service.errorCodes.SearchPlayersErrorCode;
import chess.user.model.User;
import chess.util.Result;
import chess.util.persistence.OrderBy;


@RestController
@RequestMapping(path = "/api/v1/players", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name="chess-api")
@AllArgsConstructor
public class PlayerController {

  @Autowired
  private final PlayerService playerService;

  @GetMapping("/")
  public PlayerInfo[] getPlayerInfo(@RequestParam(value="id", required=true) Long[] playerIds) {
    Result<PlayerInfo[], GetPlayerInfoErrorCode> result = playerService.getPlayerInfo(playerIds);

    if(result.code != null) {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return result.value;
  }

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

  @GetMapping("/search")
  public PlayerInfo[] searchPlayers(
    @RequestParam(value="playerType", required=true) String playerType,
    @RequestParam(value="query", required=true) String query
  ) {
    Result<PlayerInfo[], SearchPlayersErrorCode> result = playerService.searchPlayers(query, playerType);

    if(result.code != null) {
      switch(result.code) {
        case INVALID_PLAYER_TYPE:
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid player type");
        default:
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    return result.value;
  }

  @GetMapping("/{id}/games")
  public GameInfo[] getPendingGames(
    @PathVariable(value="id", required=true) long playerId,
    @RequestParam(value="status", required=false) GameStatus[] status,
    @RequestParam(value="orderBy", required=false) OrderBy orderBy,
    @Parameter(hidden=true) User user
  ) {
    Result<GameInfo[], ListGamesErrorCode> result = playerService.getGamesForPlayer(
      user.getPlayerId(),
      status == null ? new GameStatus[] {} : status,
      orderBy == null ? OrderBy.createdAt : orderBy,
      user
    );

    if(result.code != null) {
      switch(result.code) {
        case UNKOWN_PLAYER:
          throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown player");
        default:
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    return result.value;
  }
}
