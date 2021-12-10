package chess.ai.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chess.ai.model.ChessAI;
import chess.ai.model.ChessBots;
import chess.ai.service.errorCodes.ListBotsErrorCode;
import chess.player.model.PlayerInfo;
import chess.util.Result;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BotService {
  @Autowired
  private final ChessBots chessBots;

  public Result<PlayerInfo[], ListBotsErrorCode> getAvailableBots() {
    List<ChessAI> bots = chessBots.getAvailableBots();

    return new Result<PlayerInfo[], ListBotsErrorCode>(
      bots.stream().map((ChessAI bot) -> bot.info()).toArray(PlayerInfo[]::new)
    );
  }
}
