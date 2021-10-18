package chess.game.service;

import java.util.List;

import chess.MoveIntent;
import chess.PlayerColor;
import chess.game.GameInfo;
import chess.game.service.results.*;

public interface IGameService {
  List<GameInfo> listAvailableGames(long playerId);
  GameInfo getGameInfo(long gameId);
  CreateGameResult createGame(long playerId, PlayerColor playerColor, long opponentId);
  DeleteGameResult deleteGame(long gameId, long playerId);
  QuitGameResult quitGame(long gameId, long playerId);
  GameStateResult getGameState(long gameId, long playerId);
  UpdateGameResult move(long gameId, long playerId, MoveIntent moveIntent);
}
