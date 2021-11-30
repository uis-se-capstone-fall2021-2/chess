package chess.user.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import chess.player.model.PlayerRepo;

@Repository
public class Users extends PlayerRepo<User> {

  public Users(EntityManager em) {
    super(em, User.class);
  }

  public User getUserById(String userId) {
    return super.findByKey("userId", userId);
  }

  public User getUserByDisplayName(String displayName) {
    return super.getPlayerByDisplayName(displayName);
  }

  public List<User> searchUsers(String displayName) {
    return super.searchPlayers(displayName); 
  }

  @Transactional
	public User createUser(String userId, String email, String displayName) {
		Session session = getSession();

		User user = new User(userId, email);
    user.setDisplayName(displayName);

		session.save(user);
		return user;
	}
}
