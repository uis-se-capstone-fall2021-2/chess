package chess.player.model;

import java.util.*;

import chess.game.GameState;
import chess.MoveIntent;
/**
 * Player interface
 */
public interface IPlayer {
  public long getPlayerId();
  public String getDisplayName();
  public String getPlayerType();
  /** Creates and returns a PlayerInfo object
  * @return PlayerInfo player information
  */
  public PlayerInfo info();
  /** Method that is called to notify a player that they are next to move, or that they have been invited to a new game.
   * 
   * @param gamestate Gamestate of the game player is being notified about
   * @param moveHistory list of moves that happened in the game player is being notified about
   */
  public void notify(GameState gamestate, List<MoveIntent> moveHistory);
}
