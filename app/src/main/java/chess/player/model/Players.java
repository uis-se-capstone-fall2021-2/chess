package chess.player.model;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

@Repository
public class Players extends PlayerRepo<Player> {

  public Players(EntityManager em) {
    super(em, Player.class);
  }
}
