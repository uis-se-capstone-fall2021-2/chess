package chess.player.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.player.model.Player;
import chess.player.model.PlayerInfo;
import chess.player.model.Players;
import chess.player.service.errorCodes.GetPlayerInfoErrorCode;
import chess.util.Result;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PlayerService {
  
  @Autowired
  private final Players players;

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
}
