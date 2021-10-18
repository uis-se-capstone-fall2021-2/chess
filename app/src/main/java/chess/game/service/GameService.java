package chess.game.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.MoveIntent;
import chess.PlayerColor;
import chess.game.GameCompletionState;
import chess.game.GameInfo;
import chess.game.model.*;
import chess.game.service.results.*;

@Service
public class GameService implements IGameService {

  @Autowired
  Games games;

  public List<GameInfo> listAvailableGames(long playerId) {
    List<GameInfo> infos = new ArrayList<GameInfo>();
    List<Game> playerGames = games.listGamesForPlayer(playerId);
    for(Game game: playerGames) {
      infos.add(game.info());
    }
    return infos;
  }

  public GameInfo getGameInfo(long gameId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return null;
    }
    return game.info();
  }

  public CreateGameResult createGame(long playerId, PlayerColor playerColor, long opponentId) {

    if(playerId == opponentId) {
      return new CreateGameResult(CreateGameResult.Code.INVALID_OPPONENT);
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
    return new CreateGameResult(game.info());
  }

  public DeleteGameResult deleteGame(long gameId, long playerId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return DeleteGameResult.GAME_NOT_FOUND;
    } else if(game.getOwner() != playerId) {
      return DeleteGameResult.UNAUTHORIZED;
    } else if(game.getCompletionState() == GameCompletionState.ACTIVE) {
      return DeleteGameResult.GAME_ACTIVE;
    }
    games.deleteGame(gameId);
    // TODO: Notify players
    return DeleteGameResult.OK;
  }

  public QuitGameResult quitGame(long gameId, long playerId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return QuitGameResult.GAME_NOT_FOUND;
    } else if(!game.hasPlayer(playerId)) {
      return QuitGameResult.UNAUTHORIZED;
    } else if(game.getCompletionState() != GameCompletionState.ACTIVE) {
      return QuitGameResult.ALREADY_COMPLETE;
    }

    long[] players = game.getPlayers();
    long winner = players[0] == playerId
      ? players[1]
      : players[0];

    games.endGame(gameId, winner, GameCompletionState.TERMINATED);
    
    // TODO: Notify players
    return QuitGameResult.OK;
  }


  public GameStateResult getGameState(long gameId, long playerId) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return new GameStateResult(GameStateResult.Code.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(playerId)) {
      return new GameStateResult(GameStateResult.Code.UNAUTHORIZED);
    }
    return new GameStateResult(game.getGameState());
  }

  public UpdateGameResult move(long gameId, long playerId, MoveIntent moveIntent) {
    Game game = games.getGameById(gameId);
    if(game == null) {
      return new UpdateGameResult(UpdateGameResult.Code.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(playerId)) {
      return new UpdateGameResult(UpdateGameResult.Code.UNAUTHORIZED);
    } else if(game.currentPlayer() != playerId) {
      return new UpdateGameResult(UpdateGameResult.Code.OUT_OF_TURN);
    }

    boolean success = game.move(playerId, moveIntent);
    if(success) {
      // TODO: Notify players
      return new UpdateGameResult(game.getGameState());
    } else {
      return new UpdateGameResult(UpdateGameResult.Code.ILLEGAL_MOVE);
    }
  }
  
}