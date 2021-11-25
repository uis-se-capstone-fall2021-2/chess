package chess.notifications.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class NotificationService {
  @Autowired
  private final SimpMessagingTemplate template;

  public void sendGameUpdateNotificationToUser(long gameId, String userId) {
    template.convertAndSend(String.format("/users/%s/games/%d/update", gameId, userId), "{}");
  }

  public void sendGameUpdateNotification(long gameId) {
    template.convertAndSend(String.format("/games/%d/update", gameId), "{}");
  }

  public void sendGameDeleteNotification(long gameId) {
    template.convertAndSend(String.format("games/%d/delete", gameId), "{}");
  }

  public void sendPlayerStateChangeNotification(long playerId) {
    template.convertAndSend(String.format("/players/%d", playerId), "{}");
  }
}
