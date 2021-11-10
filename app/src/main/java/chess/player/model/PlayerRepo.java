package chess.player.model;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import org.hibernate.query.Query;

import chess.util.persistence.OrFilter;
import chess.util.persistence.Repo;


public abstract class PlayerRepo<T extends Player> extends Repo<T> {

  public PlayerRepo(EntityManager em, Class<T> entityClass) {
    super(em, entityClass);
  }

  public T getPlayerById(long playerId) {
    return super.findByKey("playerId", playerId);
  }

  public List<T> getPlayers(long[] playerIds) {
    Query<T> query = super.simpleFilterQuery(
      new OrFilter(Map.of(
        "playerId", playerIds
      ))
    );

    return query.getResultList();
  }

  public T getPlayerByDisplayName(String displayName) {
    return super.findByKey("displayName", displayName);
  }

  public List<T> searchPlayers(String displayName) {
    return super.search("displayName", displayName);
  }
}
