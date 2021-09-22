package chess.game.service;

import java.util.List;

import chess.game.GameInfo;
import chess.game.GameState;
import chess.game.service.params.CreateGameParams;
import chess.game.service.params.QuitGameParams;
import chess.game.service.params.UpdateGameParams;

public interface IGameService {
  List<GameInfo> listAvailableGames(long playerId);
  GameInfo getGameInfo(long gameId);
  long createGame(CreateGameParams params);
  boolean deleteGame(long gameId);
  boolean quitGame(QuitGameParams params);
  GameState getGameState(long gameId);
  GameState move(UpdateGameParams params);
}
