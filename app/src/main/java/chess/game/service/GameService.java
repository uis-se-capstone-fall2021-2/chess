package chess.game.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.game.Game;

import chess.game.GameInfo;
import chess.game.GameState;
import chess.game.service.params.*;



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
  public long createGame(CreateGameParams params) {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public boolean deleteGame(long gameId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean quitGame(QuitGameParams params) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public GameState getGameState(long gameId) {
    chess.game.db.Game record = gamesRepo.getGameById(gameId);
    if(record == null) {
      return null;
    }
    Game game = games.get(gameId);
    if(game == null) {
      // TODO: create game instance
      return null;
    }
    return game.getGameState();
  }

  @Override
  public GameState move(UpdateGameParams params) {
    // TODO Auto-generated method stub
    return null;
  }
  
}