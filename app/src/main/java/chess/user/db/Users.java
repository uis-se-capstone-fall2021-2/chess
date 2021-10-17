package chess.user.db;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

@Repository
public class Users {
  @PersistenceContext
	private EntityManager entityManager;

  private Session getSession() {
		return entityManager.unwrap(Session.class);
	}
}
