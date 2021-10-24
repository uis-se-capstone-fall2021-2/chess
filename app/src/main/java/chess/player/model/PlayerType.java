package chess.player.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PlayerType {
  User(Player.PlayerType.User),
  AI(Player.PlayerType.AI);
  
  @Getter
  private final String playerType;
}
