package chess.player.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import chess.game.GameInfo;
import chess.game.GameStatus;
import chess.game.model.Game;
import chess.game.model.Games;
import chess.player.model.Player;
import chess.player.model.PlayerInfo;
import chess.player.model.PlayerType;
import chess.player.model.Players;
import chess.player.service.errorCodes.GetPlayerInfoErrorCode;
import chess.player.service.errorCodes.ListGamesErrorCode;
import chess.user.model.User;
import chess.util.Result;
import chess.util.persistence.OrderBy;

@Service
@AllArgsConstructor
public class PlayerService {
  
  @Autowired
  private final Players players;
  @Autowired
  private final Games games;

  public Result<PlayerInfo[], GetPlayerInfoErrorCode> getPlayerInfo(long[] playerIds) {
    return new Result<PlayerInfo[], GetPlayerInfoErrorCode>(
      players.getPlayers(playerIds)
        .stream()
        .map((Player player) -> player.info())
        .toArray(PlayerInfo[]::new)
    );
  }

  public Result<PlayerInfo, GetPlayerInfoErrorCode> getPlayerInfo(long playerId) {
    Player player = players.getPlayerById(playerId);
    if(player == null) {
      return new Result<PlayerInfo, GetPlayerInfoErrorCode>(GetPlayerInfoErrorCode.NOT_FOUND);
    } else {
      return new Result<PlayerInfo, GetPlayerInfoErrorCode>(player.info());
    }
  }

  public Result<GameInfo[], ListGamesErrorCode> getGamesForPlayer(
    long playerId,
    GameStatus[] status,
    OrderBy orderBy,
    User user
  ) {
    Player player = players.getPlayerById(playerId);
    if(player == null) {
      return new Result<GameInfo[], ListGamesErrorCode>(ListGamesErrorCode.UNKOWN_PLAYER);
    } else if(player.getPlayerType() == PlayerType.AI.getPlayerType()) {
      // TODO: in the future, allow admin to view
      return new Result<GameInfo[], ListGamesErrorCode>(ListGamesErrorCode.UNAUTHORIZED);
    }
    
    if(user.getPlayerId() != playerId) {
      if(status.length == 0) {
        // if status filter is unspecified, return this default filter group
        status = new GameStatus[] {GameStatus.ACTIVE, GameStatus.COMPLETE, GameStatus.TERMINATED};
      } else {
        for(GameStatus s: status) {
          if(s == GameStatus.PENDING || s == GameStatus.DECLINED) {
            // TODO: allow admin to view
            return new Result<GameInfo[], ListGamesErrorCode>(ListGamesErrorCode.UNAUTHORIZED);
          }
        }
      }
    }

    return new Result<GameInfo[], ListGamesErrorCode>(
      games.listGamesForPlayer(playerId, status, orderBy)
        .stream()
        .map((Game game) -> game.info())
        .toArray(GameInfo[]::new)
    );
  }
}
