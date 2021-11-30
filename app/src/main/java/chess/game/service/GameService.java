package chess.game.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.MoveIntent;
import chess.PlayerColor;
import chess.game.GameStatus;
import chess.game.GameInfo;
import chess.game.GameState;
import chess.game.model.*;
import chess.game.service.errorCodes.*;
import chess.player.model.Player;
import chess.player.model.Players;
import chess.util.Result;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GameService implements IGameService {

  @Autowired
  private final Games games;
  @Autowired
  private final Players players;

  public GameInfo getGameInfo(long gameId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return null;
    }
    return game.info();
  }

  public Result<GameInfo, CreateGameErrorCode> createGame(long playerId, PlayerColor playerColor, long opponentId) {
    if(players.getPlayerById(playerId) == null) {
      return new Result<GameInfo, CreateGameErrorCode>(CreateGameErrorCode.UNKNOWN_PLAYER);
    } else if(players.getPlayerById(opponentId) == null) {
      return new Result<GameInfo, CreateGameErrorCode>(CreateGameErrorCode.UNKNOWN_OPPONENT);
    } else if(playerId == opponentId) {
      return new Result<GameInfo, CreateGameErrorCode>(CreateGameErrorCode.INVALID_OPPONENT);
    }
    
    long player1, player2, owner = playerId;

    if(playerColor == PlayerColor.WHITE) {
      player1 = owner;
      player2 = opponentId;
    } else {
      player1 = opponentId;
      player2 = owner;
    }
    Game game = games.createGame(player1, player2, owner);
    notifyPlayers(game);

    return new Result<GameInfo, CreateGameErrorCode>(game.info());
  }

  private Result<Void, GameInviteResponseErrorCode> processInviteResponse(long gameId, long playerId) {
    if(players.getPlayerById(playerId) == null) {
      return new Result<Void, GameInviteResponseErrorCode>(GameInviteResponseErrorCode.UNKNOWN_PLAYER);
    }

    Game game = games.getGameById(gameId);
    if(game == null) {
      return new Result<Void, GameInviteResponseErrorCode>(GameInviteResponseErrorCode.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(playerId)) {
      return new Result<Void, GameInviteResponseErrorCode>(GameInviteResponseErrorCode.UNAUTHORIZED);
    } else if(game.getOwner() == playerId) {
      return new Result<Void, GameInviteResponseErrorCode>(GameInviteResponseErrorCode.OWN_GAME);
    } else if(game.getStatus() != GameStatus.PENDING) {
      return new Result<Void, GameInviteResponseErrorCode>(GameInviteResponseErrorCode.WRONG_STATE);
    }

    return new Result<Void, GameInviteResponseErrorCode>();
  }

  public Result<Void, GameInviteResponseErrorCode> acceptGameInvite(long gameId, long playerId) {
    Result<Void, GameInviteResponseErrorCode> result = processInviteResponse(gameId, playerId);

    if(result.code == null) {
      Game game = games.getGameById(gameId);
      game.setStatus(GameStatus.ACTIVE);
      games.saveGame(game);
      notifyPlayers(game);
    }

    return result;
  }

  public Result<Void, GameInviteResponseErrorCode> declineGameInvite(long gameId, long playerId) {
    Result<Void, GameInviteResponseErrorCode> result = processInviteResponse(gameId, playerId);

    if(result.code == null) {
      Game game = games.getGameById(gameId);
      game.setStatus(GameStatus.DECLINED);
      games.saveGame(game);
      notifyPlayers(game);
    }

    return result;
  }

  public Result<Void, CancelGameInviteErrorCode> cancelGameInvite(long gameId, long playerId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return new Result<Void, CancelGameInviteErrorCode>(CancelGameInviteErrorCode.GAME_NOT_FOUND);
    } else if(game.getOwner() != playerId) {
      return new Result<Void, CancelGameInviteErrorCode>(CancelGameInviteErrorCode.UNAUTHORIZED);
    } else if(game.getStatus() != GameStatus.PENDING) {
      return new Result<Void, CancelGameInviteErrorCode>(CancelGameInviteErrorCode.INVALID_STATUS);
    }


    game.setStatus(GameStatus.TERMINATED);
    games.saveGame(game);
    notifyPlayers(game);
    games.deleteGame(gameId);

    return new Result<Void, CancelGameInviteErrorCode>();
  }
    

  public Result<Void, DeleteGameErrorCode> deleteGame(long gameId, long playerId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return new Result<Void, DeleteGameErrorCode>(DeleteGameErrorCode.GAME_NOT_FOUND);
    } else if(game.getOwner() != playerId) {
      return new Result<Void, DeleteGameErrorCode>(DeleteGameErrorCode.UNAUTHORIZED);
    } else if(game.getStatus() == GameStatus.ACTIVE) {
      return new Result<Void, DeleteGameErrorCode>(DeleteGameErrorCode.GAME_ACTIVE);
    }
    
    game.setStatus(GameStatus.TERMINATED);
    games.saveGame(game);
    notifyPlayers(game);
    games.deleteGame(gameId);

    return new Result<Void, DeleteGameErrorCode>();
  }

  public Result<Void, QuitGameErrorCode> quitGame(long gameId, long playerId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return new Result<Void, QuitGameErrorCode>(QuitGameErrorCode.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(playerId)) {
      return new Result<Void, QuitGameErrorCode>(QuitGameErrorCode.UNAUTHORIZED);
    } else if(game.getStatus() != GameStatus.ACTIVE) {
      return new Result<Void, QuitGameErrorCode>(QuitGameErrorCode.INACTIVE);
    }

    long[] players = game.getPlayers();
    long winner = players[0] == playerId
      ? players[1]
      : players[0];

    games.endGame(gameId, winner, GameStatus.TERMINATED);
    notifyPlayers(game);

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
    games.saveGame(game);
    if(success) {
      notifyPlayers(game);
      return new Result<GameState, UpdateGameErrorCode>(game.getGameState());
    } else {
      return new Result<GameState, UpdateGameErrorCode>(UpdateGameErrorCode.ILLEGAL_MOVE);
    }
  }

  private void notifyPlayers(Game game) {
    GameState state = game.getGameState();
    for(long playerId: game.getPlayers()) {
      Player player = players.getPlayerById(playerId);
      player.notify(state);
    }
  }
}