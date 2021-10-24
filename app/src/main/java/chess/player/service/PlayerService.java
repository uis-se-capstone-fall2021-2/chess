package chess.player.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.game.GameInfo;
import chess.game.model.Game;
import chess.game.model.Games;
import chess.player.model.Player;
import chess.player.model.PlayerInfo;
import chess.player.model.PlayerType;
import chess.player.model.Players;
import chess.player.service.errorCodes.GetPlayerInfoErrorCode;
import chess.player.service.errorCodes.ListGamesErrorCode;
import chess.util.Result;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PlayerService {
  
  @Autowired
  private final Players players;
  @Autowired
  private final Games games;

  public Result<PlayerInfo, GetPlayerInfoErrorCode> getPlayerInfo(long playerId) {
    Player player = players.getPlayerById(playerId);
    if(player == null) {
      return new Result<PlayerInfo, GetPlayerInfoErrorCode>(GetPlayerInfoErrorCode.NOT_FOUND);
    } else {
      return new Result<PlayerInfo, GetPlayerInfoErrorCode>(new PlayerInfo(
        player.getPlayerId(),
        player.getDisplayName(),
        player.getPlayerType()
      ));
    }
  }

  public Result<List<GameInfo>, ListGamesErrorCode> getActiveGamesForPlayer(long playerId) {
    Player player = players.getPlayerById(playerId);
    if(player == null) {
      return new Result<List<GameInfo> , ListGamesErrorCode>(ListGamesErrorCode.UNKOWN_PLAYER);
    } else if(player.getPlayerType() == PlayerType.AI.getPlayerType()) {
      // TODO: in the future, allow admin to view AI games
      return new Result<List<GameInfo> , ListGamesErrorCode>(ListGamesErrorCode.UNAUTHORIZED);
    }

    List<GameInfo> activeGames = new ArrayList<GameInfo>();
    List<Game> playerGames = games.listActiveGamesForPlayer(playerId);
    for(Game game: playerGames) {
      activeGames.add(game.info());
    }
    return new Result<List<GameInfo> , ListGamesErrorCode>(activeGames);
  }

  public Result<List<GameInfo>, ListGamesErrorCode> getGameHistoryForPlayer(long playerId) {
    Player player = players.getPlayerById(playerId);
    if(player == null) {
      return new Result<List<GameInfo> , ListGamesErrorCode>(ListGamesErrorCode.UNKOWN_PLAYER);
    } else if(player.getPlayerType() == PlayerType.AI.getPlayerType()) {
      // TODO: in the future, allow admin to view AI games
      return new Result<List<GameInfo> , ListGamesErrorCode>(ListGamesErrorCode.UNAUTHORIZED);
    }

    List<GameInfo> gameHistory = new ArrayList<GameInfo>();
    List<Game> playerGames = games.getGameHistoryForPlayer(playerId);
    for(Game game: playerGames) {
      gameHistory.add(game.info());
    }
    return new Result<List<GameInfo> , ListGamesErrorCode>(gameHistory);
  }
}
