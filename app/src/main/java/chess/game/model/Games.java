package chess.game.model;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import chess.util.Repo;
import chess.game.GameCompletionState;

@Repository
public class Games extends Repo {

	public List<Game> listGamesForPlayer(long playerId) {
		Session session = getSession();
		CriteriaBuilder cb = session.getCriteriaBuilder();
		CriteriaQuery<Game> q = cb.createQuery(Game.class);
		Root<Game> Game = q.from(Game.class);
		q.select(Game)
			.where(cb.or(
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
		game.setWinnner(winner);
		game.setCompletionState(completionState);
		session.saveOrUpdate(game);
	}
}