package chess.user.model;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import chess.notifications.service.NotificationService;

@Service
public class UserInterceptor extends EmptyInterceptor {
  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if(entity instanceof User) {
      NotificationService notificationService = applicationContext.getBean(NotificationService.class);
      ((User)entity).setNotificationService(notificationService);
    }
    return super.onLoad(entity, id, state, propertyNames, types);
  }
}
