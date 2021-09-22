package chess.game;

import chess.MoveIntent;
import chess.Player;

public interface IGame {
  public int gameId();
  public boolean move(Player player, MoveIntent intent);
  public GameState getGameState();
}
