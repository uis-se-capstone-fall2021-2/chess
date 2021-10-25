package chess.game.model;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;
import org.hibernate.Session;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import chess.game.GameCompletionState;
import chess.util.persistence.AndFilter;
import chess.util.persistence.OrFilter;
import chess.util.persistence.Repo;
import chess.util.persistence.PredicateList;

@Repository
public class Games extends Repo<Game> {

	public Games(EntityManager em) {
		super(em, Game.class);
	}

	public Game getGameById(long gameId) {
		Session session = getSession();
		Game game = session.get(Game.class, gameId);
		return game;
	}

	@Transactional
	public Game createGame(long player1, long player2, long owner) {
		Session session = getSession();
		Game game = new Game(player1, player2, owner);

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
	public void endGame(long gameId, long winner, GameCompletionState completionState) {
		Session session = getSession();
		Game game = session.get(Game.class, gameId);
		game.setWinner(winner);
		game.setCompletionState(completionState);
		session.saveOrUpdate(game);
	}

	public List<Game> listActiveGamesForPlayer(long playerId) {
		Query<Game> query = super.simpleFilterQuery(
			new OrFilter(Map.of(
				"player1", playerId,
				"player2", playerId
			)),
			new AndFilter(Map.of(
				"completionState", GameCompletionState.ACTIVE
			))
		);
		return query.getResultList();
	}


	public List<Game> getGameHistoryForPlayer(long playerId) {
		Session session = getSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<Game> q = cb.createQuery(entityClass);
    Root<Game> entity = q.from(entityClass);
		PredicateList<Game> predicates = new PredicateList<Game>(cb, entity);
		predicates.addOrFilter(
			new OrFilter(Map.of(
				"player1", playerId,
				"player2", playerId
			))
		);
		predicates.add(
			cb.or(
				cb.equal(entity.get("completionState"), GameCompletionState.COMPLETE),
				cb.equal(entity.get("completionState"), GameCompletionState.TERMINATED)
			)
		);
		q.select(entity).where(predicates.toArray(Predicate[]::new));
    Query<Game> query = session.createQuery(q);
		return query.getResultList();
	}
}