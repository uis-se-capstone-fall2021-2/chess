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
import chess.util.persistence.OrFilter;
import chess.util.persistence.OrderBy;
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

	// TODO does this need to be on the DBO or can we just do it in GameService?
	@Transactional
	public void endGame(long gameId, long winner, GameStatus status) {
		Session session = getSession();
		Game game = session.get(Game.class, gameId);
		game.setWinner(winner);
		game.setStatus(status);
		session.saveOrUpdate(game);
	}

	public List<Game> listGamesForPlayer(
		long playerId,
		GameStatus[] status,
		OrderBy orderBy
	) {
		Repo.CriteriaQueryConfigurer<Game> configurer = super.filterQueryFactory(
			new OrFilter(Map.of(
			"player1", playerId,
			"player2", playerId
			)),
			new OrFilter(Map.of(
				"status", status
			))
		);

		Query<Game> query = configurer.getCriteriaQuery(
			(Root<Game> $,
      CriteriaQuery<Game> q,
      CriteriaBuilder cb) -> {
				q.orderBy(cb.asc($.get(orderBy.toString())));
				q.groupBy($.get("status"), $.get("gameId"));
			});

		return query.getResultList();
	}
}