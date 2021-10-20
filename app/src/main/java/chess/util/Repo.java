package chess.util;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Repo<T> {

	@PersistenceContext
	protected EntityManager entityManager;

	protected Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	protected final Class<T> entityClass;

  protected T findByKey(String key, Object value) {
    Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(entityClass);
    Root<T> entity = q.from(entityClass);
    q.select(entity)
      .where(
        cb.equal(entity.get(key), value));
    try {
      return session.createQuery(q).getSingleResult();
    } catch(Exception e) {
      return null;
    }
  }

	protected List<T> findAllByKey(String key, Object value) {
		Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<T> q = cb.createQuery(entityClass);
    Root<T> entity = q.from(entityClass);
    q.select(entity)
      .where(
        cb.equal(entity.get(key), value));
    final List<T> results = session.createQuery(q).getResultList();

    return results;
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
