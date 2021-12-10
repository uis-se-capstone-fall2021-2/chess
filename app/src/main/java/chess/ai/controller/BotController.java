package chess.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import chess.ai.service.BotService;
import chess.ai.service.errorCodes.ListBotsErrorCode;
import chess.player.model.PlayerInfo;
import chess.util.Result;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path="/api/v1/bots", produces=MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name="chess-api")
@AllArgsConstructor
public class BotController {

  @Autowired
  private final BotService botService;

  @GetMapping(path="/")
  public PlayerInfo[] getAvailableBots() {
      Result<PlayerInfo[], ListBotsErrorCode> result = botService.getAvailableBots();
      if(result.code != null) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
      }

      return result.value;
  }
}
