package chess.ai.model;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import chess.player.model.PlayerRepo;

@Repository
public class ChessBots extends PlayerRepo<ChessAI> {
  private final static String NOVICE_BOT = "Novice Bot";
  private final static String ADVANCED_BOT = "Advanced Bot";
  
  public ChessBots(EntityManager em) {
    super(em, ChessAI.class);
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
