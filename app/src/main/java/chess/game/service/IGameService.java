package chess.game.service;

import java.util.List;

import chess.MoveIntent;
import chess.PlayerColor;
import chess.game.GameInfo;
import chess.game.GameState;
import chess.game.service.errorCodes.*;
import chess.util.Result;

public interface IGameService {
  List<GameInfo> listAvailableGames(long playerId);
  GameInfo getGameInfo(long gameId);
  Result<GameInfo, CreateGameErrorCode> createGame(long playerId, PlayerColor playerColor, long opponentId);
  Result<Void, DeleteGameErrorCode> deleteGame(long gameId, long playerId);
  Result<Void, QuitGameErrorCode> quitGame(long gameId, long playerId);
  Result<GameState, GameStateErrorCode> getGameState(long gameId, long playerId);
  Result<GameState, UpdateGameErrorCode> move(long gameId, long playerId, MoveIntent moveIntent);
}
