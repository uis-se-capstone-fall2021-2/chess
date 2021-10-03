package chess.game.service;

import java.util.List;

import chess.game.GameInfo;
import chess.game.service.params.*;
import chess.game.service.results.*;

public interface IGameService {
  List<GameInfo> listAvailableGames(long playerId);
  GameInfo getGameInfo(long gameId);
  CreateGameResult createGame(CreateGameParams params);
  DeleteGameResult deleteGame(DeleteGameParams params);
  QuitGameResult quitGame(QuitGameParams params);
  GameStateResult getGameState(GetGameStateParams params);
  UpdateGameResult move(UpdateGameParams params);
}
