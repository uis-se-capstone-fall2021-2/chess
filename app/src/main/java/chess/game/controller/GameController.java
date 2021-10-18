package chess.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import chess.MoveIntent;
import chess.game.GameInfo;
import chess.game.GameState;
import chess.game.controller.requests.CreateGameRequest;
import chess.game.service.IGameService;
import chess.game.service.results.*;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins={"*"})
@SecurityRequirement(name="chess-api")
public class GameController {

  private final static long TEST_PLAYER_ID = 1;

  private final IGameService gameService;

  @Autowired
  public GameController(IGameService gameService) {
    this.gameService = gameService;
  }


  @GetMapping("/games")
  public List<GameInfo> listAvailableGames() {
    return gameService.listAvailableGames(TEST_PLAYER_ID);
  }

  @PostMapping("/games")
  public GameInfo createGame(@RequestBody(required=true) CreateGameRequest req) {
    CreateGameResult result = gameService.createGame(
      TEST_PLAYER_ID,
      req.playerColor,
      req.opponentId
    );
    
    if(result.value != null) {
      return result.value;
    } else {
      switch(result.code) {
        case INVALID_OPPONENT:
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One's opponent cannot be oneself");
        case UNKNOWN_OPPONENT:
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown opponent");
        default:
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }

  @DeleteMapping("/games/{id}")
  public void deleteGame(@PathVariable(value="id", required=true) long gameId) {
    DeleteGameResult result = gameService.deleteGame(gameId, TEST_PLAYER_ID);
    
    switch(result) {
      case GAME_NOT_FOUND:
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      case UNAUTHORIZED:
        // only owner can delete game
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
      case GAME_ACTIVE:
        throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Cannot delete an active game");
      case OK:
      default:
        return;
    }
  }

  @PostMapping("/games/{id}/quit")
  public void quitGame(@PathVariable(value="id", required=true) long gameId) {
    QuitGameResult result = gameService.quitGame(gameId, TEST_PLAYER_ID);

    switch(result) {
      case GAME_NOT_FOUND:
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
      case UNAUTHORIZED:
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
      case ALREADY_COMPLETE:
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Game already completed");
      case OK:
      default:
        return;
    }
  }

  @GetMapping("/games/{id}")
  public GameState getGameState(@PathVariable(value="id", required=true) long gameId) {
    GameStateResult result = gameService.getGameState(gameId, TEST_PLAYER_ID);

    if(result.value == null) {
      switch(result.code) {
        case GAME_NOT_FOUND:
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        case UNAUTHORIZED:
          throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        default:
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      return result.value;
    }
  }

  @PatchMapping("/games/{id}")
  public GameState move(
    @PathVariable(value="id", required=true) long gameId,
    @RequestBody(required=true) MoveIntent moveIntent
  ) {
    UpdateGameResult result = gameService.move(
      gameId,
      TEST_PLAYER_ID,
      moveIntent
    );

    if(result.value == null) {
      switch(result.code) {
        case GAME_NOT_FOUND:
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        case UNAUTHORIZED:
          throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        case OUT_OF_TURN:
          throw new ResponseStatusException(HttpStatus.LOCKED, "Cannot move out of turn");
        case ILLEGAL_MOVE:
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Illegal move");
        default:
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      return result.value;
    }
  }

}