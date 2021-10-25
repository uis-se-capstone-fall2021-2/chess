package chess.util.persistence;

import java.util.List;
import java.util.Map;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.query.Query;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Repo<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	protected final Class<T> entityClass;

  protected Query<T> simpleFilterQuery(OrFilter orFilter) {
    return simpleFilterQuery(orFilter, new AndFilter());
  }

  protected Query<T> simpleFilterQuery(AndFilter andFilter) {
    return simpleFilterQuery(new OrFilter(), andFilter);
  }

  protected Query<T> simpleFilterQuery(OrFilter orFilter, AndFilter andFilter) {
    Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(entityClass);
    Root<T> entity = q.from(entityClass);
    PredicateList<T> predicates = new PredicateList<T>(cb, entity);
    predicates.addOrFilter(orFilter);
    predicates.addAndFilter(andFilter);
    q.select(entity).where(predicates.toArray(Predicate[]::new));
    Query<T> query = session.createQuery(q);
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
    Root<T> entity = q.from(entityClass);
    q.select(entity)
      .where(
        cb.like(entity.get(key), value));
    final List<T> results = session.createQuery(q).getResultList();

    return results;
  }
}
