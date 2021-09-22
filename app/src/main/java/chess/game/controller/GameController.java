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
import chess.game.service.params.CreateGameParams;
import chess.game.service.params.QuitGameParams;
import chess.game.service.params.UpdateGameParams;

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
  public long createGame(@RequestBody(required=true) CreateGameRequest req) {
    return gameService.createGame(
      new CreateGameParams(
        mockPlayerIdFromRequest,
        req.opponentId,
        req.player1Color));
  }

  @DeleteMapping("/games/{id}")
  public boolean deleteGame(@PathVariable(value="id", required=true) long gameId) {
    GameInfo gameInfo = gameService.getGameInfo(gameId);
    if(gameInfo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } else if(mockPlayerIdFromRequest != gameInfo.owner) {
      // only owner can delete game
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    // TODO: GameService impl must reject if game is active
    return gameService.deleteGame(gameId);
  }

  @PostMapping("/games/{id}/quit")
  public boolean quitGame(@PathVariable(value="id", required=true) long gameId) {
    GameInfo gameInfo = gameService.getGameInfo(gameId);
    if(gameInfo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } else if(!GameInfo.playerIsInGame(gameInfo, mockPlayerIdFromRequest)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } 
    return gameService.quitGame(
      new QuitGameParams(
        gameId,
        mockPlayerIdFromRequest));
  }

  @GetMapping("/games/{id}")
  public GameState getGameState(@PathVariable(value="id", required=true) long gameId) {
    GameInfo gameInfo = gameService.getGameInfo(gameId);
    if(gameInfo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } else if(!GameInfo.playerIsInGame(gameInfo, mockPlayerIdFromRequest)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    } 
    // TODO: GameService impl must instantiate a new Game from db if no game is loaded
    return gameService.getGameState(gameId);
  }

  @PatchMapping("/games/{id}")
  public GameState move(
    @PathVariable(value="id", required=true) long gameId,
    @RequestBody(required=true) MoveIntent moveIntent
  ) {
    GameInfo gameInfo = gameService.getGameInfo(gameId);
    if(gameInfo == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    } else if(!GameInfo.playerIsInGame(gameInfo, mockPlayerIdFromRequest)) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    // TODO: GameService impl must reject if move is invalid
    return gameService.move(
      new UpdateGameParams(
        mockPlayerIdFromRequest,
        moveIntent
      ));
  }

}