package chess.game.db;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;


@Repository
public class Games {

	@PersistenceContext
  private EntityManager entityManager;
	
	public List<Game> listGamesForPlayer(long playerId) {
		Session session  = entityManager.unwrap(Session.class);
		session.beginTransaction();
		String q = "FROM Games G WHERE some elements(G.players) = :playerId";
		Query<Game> query = session.createQuery(q, Game.class);
		query.setParameter("gameId", playerId);
		List<Game> games = query.list();
		session.getTransaction().commit();
		session.close();
		return games;
	}

	public Game getGameById(long gameId) {
		Session session  = entityManager.unwrap(Session.class);
		session.beginTransaction();
		Game game = session.get(Game.class, gameId);
		session.getTransaction().commit();
		session.close();
		return game;
	}
}