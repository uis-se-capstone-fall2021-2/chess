package chess.player.model;

import java.util.List;

import javax.persistence.EntityManager;

import chess.util.persistence.Repo;


public abstract class PlayerRepo<T extends Player> extends Repo<T> {

  public PlayerRepo(EntityManager em, Class<T> entityClass) {
    super(em, entityClass);
  }

  public T getPlayerById(long playerId) {
    return super.findByKey("playerId", playerId);
  }

  public T getPlayerByDisplayName(String displayName) {
    return super.findByKey("displayName", displayName);
  }

  public List<T> searchPlayers(String displayName) {
    return super.search("displayName", displayName);
  }
}
