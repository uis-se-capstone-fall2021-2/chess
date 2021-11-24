package chess.player.model;

import chess.game.GameState;

public interface IPlayer {
  public long getPlayerId();
  public String getDisplayName();
  public String getPlayerType();
  public PlayerInfo info();
  public void notify(GameState gamestate);
}
