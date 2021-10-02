package chess.game.db;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import chess.game.db.params.*;


@Repository
public class Games {

	@PersistenceContext
  private EntityManager entityManager;

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	public List<Game> listGamesForPlayer(long playerId) {
		Session session = getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Game> q = cb.createQuery(Game.class);
		Root<Game> Game = q.from(Game.class);
		q.select(Game)
			.where(
				cb.or(
					cb.equal(Game.get("player1"), playerId),
					cb.equal(Game.get("player2"), playerId)));
		final List<Game> results = session.createQuery(q).getResultList();
		return results;
	}

	public Game getGameById(long gameId) {
		Session session = getSession();
		Game game = session.get(Game.class, gameId);
		return game;
	}

	@Transactional
	public Game createGame(CreateGameParams params) {
		Session session = getSession();

		Game game = new Game(
			params.player1,
			params.player2,
			params.owner
		);
		
		session.save(game);
		return game;
	}

	@Transactional
	public void deleteGame(long gameId) {
		Session session = getSession();
		Game game = session.get(Game.class, gameId);
		session.remove(game);
	}

	@Transactional
	public void endGame(EndGameParams params) {
		Session session = getSession();
		Game game = session.get(Game.class, params.gameId);
		game.setWinnner(params.winner);
		game.setCompletionState(params.completionState);
		session.saveOrUpdate(game);
	}
}