package chess.game.model;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.query.Query;
import org.hibernate.Session;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import chess.game.GameStatus;
import chess.util.persistence.AndFilter;
import chess.util.persistence.OrFilter;
import chess.util.persistence.Repo;

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
	public void endGame(long gameId, long winner, GameStatus status) {
		Session session = getSession();
		Game game = session.get(Game.class, gameId);
		game.setWinner(winner);
		game.setCompletionState(status);
		session.saveOrUpdate(game);
	}

	public List<Game> listActiveGamesForPlayer(long playerId) {
		Repo.CriteriaQueryConfigurer<Game> configurer =super.filterQueryFactory(
			new OrFilter(Map.of(
				"player1", playerId,
				"player2", playerId
			)),
			new AndFilter(Map.of(
				"status", GameStatus.ACTIVE
			))
		);

		Query<Game> query = configurer.getCriteriaQuery(
			(Root<Game> $,
      CriteriaQuery<Game> q,
      CriteriaBuilder cb) -> {
				q.orderBy(cb.asc($.get("updatedAt")));
			});

		return query.getResultList();
	}


	public List<Game> getGameHistoryForPlayer(long playerId) {
		GameStatus[] completeStatuses = {
			GameStatus.COMPLETE,
			GameStatus.TERMINATED
		};
		Repo.CriteriaQueryConfigurer<Game> configurer = super.filterQueryFactory(
			new OrFilter(Map.of(
			"player1", playerId,
			"player2", playerId
			)),
			new OrFilter(Map.of(
				"status", completeStatuses
			))
		);

		Query<Game> query = configurer.getCriteriaQuery(
			(Root<Game> $,
      CriteriaQuery<Game> q,
      CriteriaBuilder cb) -> {
				q.orderBy(cb.asc($.get("completedAt")));
			});

		return query.getResultList();
	}
}