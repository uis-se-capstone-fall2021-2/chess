package chess.player.model;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
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

  public List<T> getPlayers(Long[] playerIds) {
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

  public List<T> searchPlayers(String query) {
    Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(entityClass);
    Root<T> $ = q.from(entityClass);
    q.select($)
      .where(cb.like($.get("displayName"), "%" + query + "%"));
    final List<T> results = session.createQuery(q).getResultList();

    return results;
  }
}
