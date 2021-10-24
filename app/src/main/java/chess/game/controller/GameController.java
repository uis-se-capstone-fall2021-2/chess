package chess.game.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import chess.MoveIntent;
import chess.game.GameInfo;
import chess.game.GameState;
import chess.game.controller.requests.CreateGameRequest;
import chess.game.service.IGameService;
import chess.game.service.errorCodes.*;
import chess.user.model.User;
import chess.util.Result;

@RestController
@RequestMapping(path = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins={"*"})
@SecurityRequirement(name="chess-api")
@AllArgsConstructor
public class GameController {

  @Autowired
  private final IGameService gameService;

  @PostMapping("/games")
  public GameInfo createGame(
    @Parameter(hidden=true) User user,
    @RequestBody(required=true) CreateGameRequest req
  ) {
    Result<GameInfo, CreateGameErrorCode> result = gameService.createGame(
      user.getPlayerId(),
      req.playerColor,
      req.opponentId
    );
    
    if(result.code != null) {
      switch(result.code) {
        case INVALID_OPPONENT:
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One's opponent cannot be oneself");
        case UNKNOWN_OPPONENT:
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown opponent");
        default:
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    } else {
      return result.value;
    }
  }

  @DeleteMapping("/games/{id}")
  public void deleteGame(
    @Parameter(hidden=true) User user,
    @PathVariable(value="id", required=true) long gameId
  ) {
    Result<Void, DeleteGameErrorCode> result = gameService.deleteGame(gameId, user.getPlayerId());
    
    if(result.code != null) {
      switch(result.code) {
        case GAME_NOT_FOUND:
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        case UNAUTHORIZED:
          // only owner can delete game
          throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        case GAME_ACTIVE:
          throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Cannot delete an active game");
        default:
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }

  @PostMapping("/games/{id}/quit")
  public void quitGame(
    @Parameter(hidden=true) User user,
    @PathVariable(value="id", required=true) long gameId
  ) {
    Result<Void, QuitGameErrorCode> result = gameService.quitGame(gameId, user.getPlayerId());

    if(result.code != null) {
      switch(result.code) {
        case GAME_NOT_FOUND:
          throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        case UNAUTHORIZED:
          throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        case ALREADY_COMPLETE:
          throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Game already completed");
        default:
          throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }

  @GetMapping("/games/{id}")
  public GameState getGameState(
    @Parameter(hidden=true) User user,
    @PathVariable(value="id", required=true) long gameId
  ) {
    Result<GameState, GameStateErrorCode> result = gameService.getGameState(gameId, user.getPlayerId());

    if(result.code != null) {
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
    @Parameter(hidden=true) User user,
    @PathVariable(value="id", required=true) long gameId,
    @RequestBody(required=true) MoveIntent moveIntent
  ) {
    Result<GameState, UpdateGameErrorCode> result = gameService.move(
      gameId,
      user.getPlayerId(),
      moveIntent
    );

    if(result.code != null) {
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