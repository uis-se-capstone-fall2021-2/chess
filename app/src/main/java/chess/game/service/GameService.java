package chess.game.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.PlayerColor;
import chess.game.GameCompletionState;
import chess.game.GameInfo;
import chess.game.model.*;
import chess.game.service.params.*;
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

  public CreateGameResult createGame(CreateGameParams params) {

    if(params.playerId == params.opponentId) {
      return new CreateGameResult(CreateGameResult.Code.INVALID_OPPONENT);
    }
    

    long player1, player2, owner = params.playerId;
   
    /*
    if(! both players exist in system) {
      // TODO: check that both players exist
      return new CreateGameResult(CreateGameResult.Code.UNKNOWN_OPPONENT);
    }
    */

    if(params.playerColor == PlayerColor.WHITE) {
      player1 = owner;
      player2 = params.opponentId;
    } else {
      player1 = params.opponentId;
      player2 = owner;
    }
    Game game = games.createGame(
      new chess.game.model.params.CreateGameParams(
        player1,
        player2,
        owner
      ));
    // TODO: notify players
    return new CreateGameResult(game.info());
  }

  public DeleteGameResult deleteGame(DeleteGameParams params) {
    Game game = games.getGameById(params.gameId);
    if(game == null) {
      return DeleteGameResult.GAME_NOT_FOUND;
    } else if(game.getOwner() != params.playerId) {
      return DeleteGameResult.UNAUTHORIZED;
    } else if(game.getCompletionState() == GameCompletionState.ACTIVE) {
      return DeleteGameResult.GAME_ACTIVE;
    }
    games.deleteGame(params.gameId);
    // TODO: Notify players
    return DeleteGameResult.OK;
  }

  public QuitGameResult quitGame(QuitGameParams params) {
    Game game = games.getGameById(params.gameId);
    if(game == null) {
      return QuitGameResult.GAME_NOT_FOUND;
    } else if(!game.hasPlayer(params.playerId)) {
      return QuitGameResult.UNAUTHORIZED;
    } else if(game.getCompletionState() != GameCompletionState.ACTIVE) {
      return QuitGameResult.ALREADY_COMPLETE;
    }

    long[] players = game.getPlayers();
    long winner = players[0] == params.playerId
      ? players[1]
      : players[0];

    games.endGame(
      new chess.game.model.params.EndGameParams(
        params.gameId,
        winner,
        GameCompletionState.TERMINATED
      ));
    
    // TODO: Notify players
    return QuitGameResult.OK;
  }


  public GameStateResult getGameState(GetGameStateParams params) {
    Game game = games.getGameById(params.gameId);
    if(game == null) {
      return new GameStateResult(GameStateResult.Code.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(params.playerId)) {
      return new GameStateResult(GameStateResult.Code.UNAUTHORIZED);
    }
    return new GameStateResult(game.getGameState());
  }

  public UpdateGameResult move(UpdateGameParams params) {
    Game game = games.getGameById(params.gameId);
    if(game == null) {
      return new UpdateGameResult(UpdateGameResult.Code.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(params.playerId)) {
      return new UpdateGameResult(UpdateGameResult.Code.UNAUTHORIZED);
    } else if(game.currentPlayer() != params.playerId) {
      return new UpdateGameResult(UpdateGameResult.Code.OUT_OF_TURN);
    }

    boolean success = game.move(params.playerId, params.moveIntent);
    if(success) {
      // TODO: Notify players
      return new UpdateGameResult(game.getGameState());
    } else {
      return new UpdateGameResult(UpdateGameResult.Code.ILLEGAL_MOVE);
    }

    
  }
  
}