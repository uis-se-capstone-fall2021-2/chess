package chess.game.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import chess.MoveIntent;
import chess.game.GameInfo;
import chess.game.GameState;
import chess.game.controller.requests.CreateGameRequest;
import chess.game.service.IGameService;
import chess.game.service.params.*;
import chess.game.service.results.*;


@RestController
public class GameController {

  private final static long mockPlayerIdFromRequest = 1;

  private final IGameService gameService;

  @Autowired
  public GameController(IGameService gameService) {
    this.gameService = gameService;
  }

  @GetMapping("/games")
  public List<GameInfo> listAvailableGames() {
    return gameService.listAvailableGames(mockPlayerIdFromRequest);
  }

  @PostMapping("/games")
  public GameInfo createGame(@RequestBody(required=true) CreateGameRequest req) {
    return gameService.createGame(
      new CreateGameParams(
        mockPlayerIdFromRequest,
        req.opponentId,
        req.player1Color));
  }

  @DeleteMapping("/games/{id}")
  public void deleteGame(@PathVariable(value="id", required=true) long gameId) {
    DeleteGameResult result = gameService.deleteGame(
      new DeleteGameParams(gameId, mockPlayerIdFromRequest));
    
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
    QuitGameResult result = gameService.quitGame(
      new QuitGameParams(
        gameId,
        mockPlayerIdFromRequest));

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
    GameStateResult result = gameService.getGameState(
      new GetGameStateParams(gameId, mockPlayerIdFromRequest));

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
      new UpdateGameParams(
        gameId,
        mockPlayerIdFromRequest,
        moveIntent
      ));
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