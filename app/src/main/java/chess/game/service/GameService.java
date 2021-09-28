package chess.game.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.game.Game;
import chess.game.GameCompletionState;
import chess.game.GameInfo;
import chess.game.GameState;
import chess.game.service.params.*;
import chess.game.service.results.*;



@Service
public class GameService implements IGameService {
  private final HashMap<Long, Game> games = new HashMap<Long, Game>();

  @Autowired
  chess.game.db.Games gamesRepo;

  public List<GameInfo> listAvailableGames(long playerId) {
    List<GameInfo> infos = new ArrayList<GameInfo>();
    List<chess.game.db.Game> records = gamesRepo.listGamesForPlayer(playerId);
    for(chess.game.db.Game record: records) {
      infos.add(record.info());
    }
    return infos;

  }

  public GameInfo getGameInfo(long gameId) {
    chess.game.db.Game record = gamesRepo.getGameById(gameId);
    if(record == null) {
      return null;
    }
    return record.info();
  }

  @Override
  public GameInfo createGame(CreateGameParams params) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DeleteGameResult deleteGame(DeleteGameParams params) {
    chess.game.db.Game record = gamesRepo.getGameById(params.gameId);
    if(record == null) {
      return DeleteGameResult.GAME_NOT_FOUND;
    } else if(record.getOwner() != params.playerId) {
      return DeleteGameResult.UNAUTHORIZED;
    } else if(record.getCompletionState() == GameCompletionState.ACTIVE) {
      return DeleteGameResult.GAME_ACTIVE;
    }
    // TODO: gameRepo.deleteGame(gameId)
    return DeleteGameResult.OK;
  }

  @Override
  public QuitGameResult quitGame(QuitGameParams params) {
    chess.game.db.Game record = gamesRepo.getGameById(params.gameId);
    if(record == null) {
      return QuitGameResult.GAME_NOT_FOUND;
    } else if(!GameInfo.playerIsInGame(record.info(), params.playerId)) {
      return QuitGameResult.UNAUTHORIZED;
    } else if(record.getCompletionState() != GameCompletionState.ACTIVE) {
      return QuitGameResult.ALREADY_COMPLETE;
    }
    // TODO: quit game, save, notify players
    return QuitGameResult.OK;
  }

  @Override
  public GameStateResult getGameState(GetGameStateParams params) {
    chess.game.db.Game record = gamesRepo.getGameById(params.gameId);
    if(record == null) {
      return new GameStateResult(GameStateResult.Code.GAME_NOT_FOUND);
    } else if(!GameInfo.playerIsInGame(record.info(), params.playerId)) {
      return new GameStateResult(GameStateResult.Code.UNAUTHORIZED);
    }
    Game game = games.get(params.gameId);
    if(game == null) {
      // TODO: create game instance
      new GameStateResult(GameStateResult.Code.GAME_NOT_FOUND);
    }
    return new GameStateResult(game.getGameState());
  }

  @Override
  public UpdateGameResult move(UpdateGameParams params) {
    chess.game.db.Game record = gamesRepo.getGameById(params.gameId);
    if(record == null) {
      return new UpdateGameResult(UpdateGameResult.Code.GAME_NOT_FOUND);
    } else if(!GameInfo.playerIsInGame(record.info(), params.playerId)) {
      return new UpdateGameResult(UpdateGameResult.Code.UNAUTHORIZED);
    }
    // TODO: implement
    return new UpdateGameResult(UpdateGameResult.Code.OUT_OF_TURN);
  }
  
}