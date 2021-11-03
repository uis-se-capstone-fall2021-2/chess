package chess.util.persistence;

import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.query.Query;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Repo<T> {

  public static interface QueryRefiner<T> {
    void refineQuery(
      Root<T> $,
      CriteriaQuery<T> criteriaQuery,
      CriteriaBuilder criteriaBuilder
    );
  }

  public static interface CriteriaQueryConfigurer<T> {
    Query<T> getCriteriaQuery(QueryRefiner<T> refiner);
  }

	@PersistenceContext
	protected EntityManager entityManager;

	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	protected final Class<T> entityClass;

  protected CriteriaQueryConfigurer<T> filterQueryFactory(Filter ...filters) {
    Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(entityClass);
    Root<T> $ = q.from(entityClass);
    FilterBuilder<T> fb = new FilterBuilder<T>(cb, $);
    fb.addFilters(filters);
    q.select($).where(fb.toArray());
    return (QueryRefiner<T> refiner) -> {
      refiner.refineQuery($, q, cb);
      return session.createQuery(q);
    };
  }

  protected Query<T> simpleFilterQuery(Filter... filters) {
    CriteriaQueryConfigurer<T> configurer = filterQueryFactory(filters);
    Query<T> query = configurer.getCriteriaQuery(
      // noop
      (Root<T> $,
      CriteriaQuery<T> criteriaQuery,
      CriteriaBuilder criteriaBuilder) -> {}
    );
    return query;
  }

  protected T findByKey(String key, Object value) {
    Query<T> query = simpleFilterQuery(new AndFilter(Map.of(key, value)));
    try {
      return query.getSingleResult();
    } catch(Exception e) {
      return null;
    }
  }

	protected List<T> findAllByKey(String key, Object value) {
    Query<T> query = simpleFilterQuery(new AndFilter(Map.of(key, value)));
    return query.getResultList();
	}

	protected List<T> search(String key, String value) {
    Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(entityClass);
    Root<T> $ = q.from(entityClass);
    q.select($)
      .where(
        cb.like($.get(key), value));
    final List<T> results = session.createQuery(q).getResultList();

    return results;
  }
}
