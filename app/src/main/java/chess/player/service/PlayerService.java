package chess.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import chess.game.GameInfo;
import chess.game.GameStatus;
import chess.game.model.Game;
import chess.game.model.Games;
import chess.player.model.Player;
import chess.player.model.PlayerInfo;
import chess.player.model.PlayerRepo;
import chess.player.model.PlayerType;
import chess.player.model.Players;
import chess.player.service.errorCodes.GetPlayerInfoErrorCode;
import chess.player.service.errorCodes.ListGamesErrorCode;
import chess.player.service.errorCodes.SearchPlayersErrorCode;
import chess.user.model.User;
import chess.user.model.Users;
import chess.util.Result;
import chess.util.persistence.OrderBy;

@Service
@AllArgsConstructor
public class PlayerService {
  
  @Autowired
  private final Players players;
  @Autowired
  private final Users users;
  @Autowired
  private final Games games;

  public Result<PlayerInfo[], GetPlayerInfoErrorCode> getPlayerInfo(Long[] playerIds) {
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

  public Result<PlayerInfo[], SearchPlayersErrorCode> searchPlayers(String query, String playerType) {

    PlayerRepo<? extends Player> repo;
    switch(playerType) {
      case "ANY":
        repo = players;
        break;
      case Player.PlayerType.User:
        repo = users;
        break;
      case Player.PlayerType.AI:
        // TODO: currently no ChessBot class extending Player
        return new Result<PlayerInfo[], SearchPlayersErrorCode>(new PlayerInfo[] {});
      default:
        return new Result<PlayerInfo[], SearchPlayersErrorCode>(SearchPlayersErrorCode.INVALID_PLAYER_TYPE);
    }
  
    return new Result<PlayerInfo[], SearchPlayersErrorCode>(
      players.searchPlayers(query)
        .stream()
        .map((Player player) -> player.info())
        .toArray(PlayerInfo[]::new)
    );
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
