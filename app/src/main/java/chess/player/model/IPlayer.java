package chess.player.model;

import java.util.*;

import chess.game.GameState;
import chess.MoveIntent;

public interface IPlayer {
  public long getPlayerId();
  public String getDisplayName();
  public String getPlayerType();
  public PlayerInfo info();
  public void notify(GameState gamestate, List<MoveIntent> moveHistory);
}
