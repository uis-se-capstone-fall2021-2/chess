package chess.player.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * container to pass player info to client
 */
@AllArgsConstructor
public class PlayerInfo {
  @Getter
  private long playerId;
  @Getter
  private String displayName;
  @Getter
  private String playerType;
}
