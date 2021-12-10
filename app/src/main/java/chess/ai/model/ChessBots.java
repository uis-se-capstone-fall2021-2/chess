package chess.ai.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import chess.player.model.PlayerRepo;
import chess.util.persistence.Repo;

/**
 * Responsible for creating the Chess AI players in the app.
 */
@Repository
public class ChessBots extends PlayerRepo<ChessAI> {
  private final static String NOVICE_BOT = "Novice Bot";
  private final static String ADVANCED_BOT = "Advanced Bot";
  
  public ChessBots(EntityManager em) {
    super(em, ChessAI.class);
  }

  public List<ChessAI> getAvailableBots() {
    Repo.CriteriaQueryConfigurer<ChessAI> configurer = filterQueryFactory(/* no filter, get all */);
    Query<ChessAI> allQuery = configurer.getCriteriaQuery((
      Root<ChessAI> $,
      CriteriaQuery<ChessAI> q,
      CriteriaBuilder cb
    ) -> {
      q.orderBy(cb.desc($.get("displayName")));
    });
    return allQuery.getResultList();
  }
  
  public void createBotsAsNeeded() {
    createBeginnerBotAsNeeded();
    createAdvancedBotAsNeeded();
  }

  private void createBeginnerBotAsNeeded() {
    Session session = getSession();
    if(getPlayerByDisplayName(NOVICE_BOT) == null) {
      Beginner bot = new Beginner();
      bot.setDisplayName(NOVICE_BOT);
      session.save(bot);
    }
  }

  private void createAdvancedBotAsNeeded() {
    Session session = getSession();
    if(getPlayerByDisplayName(ADVANCED_BOT) == null) {
      Advanced bot = new Advanced();
      bot.setDisplayName(ADVANCED_BOT);
      session.save(bot);
    }
    
  }
}
