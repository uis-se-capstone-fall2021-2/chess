package chess.game.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.MoveIntent;
import chess.PlayerColor;
import chess.game.GameCompletionState;
import chess.game.GameInfo;
import chess.game.GameState;
import chess.game.model.*;
import chess.game.service.errorCodes.*;
import chess.util.Result;

@Service
public class GameService implements IGameService {

  @Autowired
  Games games;

  public GameInfo getGameInfo(long gameId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return null;
    }
    return game.info();
  }

  public Result<GameInfo, CreateGameErrorCode> createGame(long playerId, PlayerColor playerColor, long opponentId) {

    if(playerId == opponentId) {
      return new Result<GameInfo, CreateGameErrorCode>(CreateGameErrorCode.INVALID_OPPONENT);
    }
    

    long player1, player2, owner = playerId;
   
    /*
    if(! both players exist in system) {
      // TODO: check that both players exist
      return new CreateGameResult(CreateGameResult.Code.UNKNOWN_OPPONENT);
    }
    */

    if(playerColor == PlayerColor.WHITE) {
      player1 = owner;
      player2 = opponentId;
    } else {
      player1 = opponentId;
      player2 = owner;
    }
    Game game = games.createGame(player1, player2, owner);
    // TODO: notify players
    return new Result<GameInfo, CreateGameErrorCode>(game.info());
  }

  public Result<Void, DeleteGameErrorCode> deleteGame(long gameId, long playerId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return new Result<Void, DeleteGameErrorCode>(DeleteGameErrorCode.GAME_NOT_FOUND);
    } else if(game.getOwner() != playerId) {
      return new Result<Void, DeleteGameErrorCode>(DeleteGameErrorCode.UNAUTHORIZED);
    } else if(game.getCompletionState() == GameCompletionState.ACTIVE) {
      return new Result<Void, DeleteGameErrorCode>(DeleteGameErrorCode.GAME_ACTIVE);
    }
    games.deleteGame(gameId);
    // TODO: Notify players
    return new Result<Void, DeleteGameErrorCode>();
  }

  public Result<Void, QuitGameErrorCode> quitGame(long gameId, long playerId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return new Result<Void, QuitGameErrorCode>(QuitGameErrorCode.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(playerId)) {
      return new Result<Void, QuitGameErrorCode>(QuitGameErrorCode.UNAUTHORIZED);
    } else if(game.getCompletionState() != GameCompletionState.ACTIVE) {
      return new Result<Void, QuitGameErrorCode>(QuitGameErrorCode.ALREADY_COMPLETE);
    }

    long[] players = game.getPlayers();
    long winner = players[0] == playerId
      ? players[1]
      : players[0];

    games.endGame(gameId, winner, GameCompletionState.TERMINATED);
    
    // TODO: Notify players
    return new Result<Void, QuitGameErrorCode>();
  }


  public Result<GameState, GameStateErrorCode> getGameState(long gameId, long playerId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return new Result<GameState, GameStateErrorCode>(GameStateErrorCode.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(playerId)) {
      return new Result<GameState, GameStateErrorCode>(GameStateErrorCode.UNAUTHORIZED);
    }
    return new Result<GameState, GameStateErrorCode>(game.getGameState());
  }

  public Result<GameState, UpdateGameErrorCode> move(long gameId, long playerId, MoveIntent moveIntent) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return new Result<GameState, UpdateGameErrorCode>(UpdateGameErrorCode.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(playerId)) {
      return new Result<GameState, UpdateGameErrorCode>(UpdateGameErrorCode.UNAUTHORIZED);
    } else if(game.currentPlayer() != playerId) {
      return new Result<GameState, UpdateGameErrorCode>(UpdateGameErrorCode.OUT_OF_TURN);
    }

    boolean success = game.move(playerId, moveIntent);
    if(success) {
      // TODO: Notify players
      return new Result<GameState, UpdateGameErrorCode>(game.getGameState());
    } else {
      return new Result<GameState, UpdateGameErrorCode>(UpdateGameErrorCode.ILLEGAL_MOVE);
    }
  }
  
}