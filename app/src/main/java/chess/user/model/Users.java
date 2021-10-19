package chess.user.model;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Session;

import org.springframework.stereotype.Repository;

import chess.util.Repo;

@Repository
public class Users extends Repo {
  public User getUserById(String userId) {
    Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<User> q = cb.createQuery(User.class);
    Root<User> user = q.from(User.class);
    q.select(user)
      .where(
        cb.equal(user.get("userId"), userId));
    try {
      return session.createQuery(q).getSingleResult();
    } catch(Exception e) {
      return null;
    }
  }

  public User getUserByDisplayName(String displayName) {
    Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<User> q = cb.createQuery(User.class);
    Root<User> user = q.from(User.class);
    q.select(user)
      .where(
        cb.equal(user.get("displayName"), displayName));
    try {
      return session.createQuery(q).getSingleResult();
    } catch(Exception e) {
      return null;
    }
  }

  public List<User> searchUsers(String displayName) {
    Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<User> q = cb.createQuery(User.class);
    Root<User> user = q.from(User.class);
    q.select(user)
      .where(
        cb.like(user.get("displayName"), displayName));
    final List<User> results = session.createQuery(q).getResultList();

    return results; 
  }

  @Transactional
	public User createUser(String userId, String email, String displayName) {
		Session session = getSession();

		User user = new User(userId, email, displayName);

		session.save(user);
		return user;
	}
}
