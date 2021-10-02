package chess.game.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.PlayerColor;
import chess.game.GameCompletionState;
import chess.game.GameInfo;
import chess.game.service.params.*;
import chess.game.service.results.*;



@Service
public class GameService implements IGameService {

  @Autowired
  chess.game.db.Games games;

  public List<GameInfo> listAvailableGames(long playerId) {
    List<GameInfo> infos = new ArrayList<GameInfo>();
    List<chess.game.db.Game> playerGames = games.listGamesForPlayer(playerId);
    for(chess.game.db.Game game: playerGames) {
      infos.add(game.info());
    }
    return infos;
  }

  public GameInfo getGameInfo(long gameId) {
    chess.game.db.Game game = games.getGameById(gameId);
    if(game == null) {
      return null;
    }
    return game.info();
  }

  public GameInfo createGame(CreateGameParams params) {
    long player1, player2, owner = params.playerId;
    if(params.playerColor == PlayerColor.WHITE) {
      player1 = owner;
      player2 = params.opponentId;
    } else {
      player1 = params.opponentId;
      player2 = owner;
    }
    chess.game.db.Game game = games.createGame(
      new chess.game.db.params.CreateGameParams(
        player1,
        player2,
        owner
      ));
    // TODO: notify players
    return game.info();
  }

  public DeleteGameResult deleteGame(DeleteGameParams params) {
    chess.game.db.Game game = games.getGameById(params.gameId);
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
    chess.game.db.Game game = games.getGameById(params.gameId);
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
      new chess.game.db.params.EndGameParams(
        params.gameId,
        winner,
        GameCompletionState.TERMINATED
      ));
    
    // TODO: Notify players
    return QuitGameResult.OK;
  }


  public GameStateResult getGameState(GetGameStateParams params) {
    chess.game.db.Game game = games.getGameById(params.gameId);
    if(game == null) {
      return new GameStateResult(GameStateResult.Code.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(params.playerId)) {
      return new GameStateResult(GameStateResult.Code.UNAUTHORIZED);
    }
    return new GameStateResult(game.getGameState());
  }

  public UpdateGameResult move(UpdateGameParams params) {
    chess.game.db.Game game = games.getGameById(params.gameId);
    if(game == null) {
      return new UpdateGameResult(UpdateGameResult.Code.GAME_NOT_FOUND);
    } else if(!game.hasPlayer(params.playerId)) {
      return new UpdateGameResult(UpdateGameResult.Code.UNAUTHORIZED);
    }

    game.move(params.playerId, params.moveIntent);
    // TODO: Notify players
    return new UpdateGameResult(game.getGameState());
  }
  
}