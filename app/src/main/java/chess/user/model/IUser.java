package chess.user.model;

import chess.player.model.IPlayer;

public interface IUser extends IPlayer {
  public String getUserId();
  public String getEmail();
}
