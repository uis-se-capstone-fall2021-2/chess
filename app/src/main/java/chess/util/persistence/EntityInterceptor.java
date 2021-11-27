package chess.util.persistence;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class EntityInterceptor extends EmptyInterceptor {
  @Autowired
  private ApplicationContext applicationContext;

  @Override
  public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    if(entity instanceof ContextAwareEntity) {
      ApplicationContextProvider.setApplicationContext((ContextAwareEntity)entity, applicationContext);
    }
    return super.onLoad(entity, id, state, propertyNames, types);
  }

  private static class ApplicationContextProvider extends ContextAwareEntity {
    public static void setApplicationContext(ContextAwareEntity entity, ApplicationContext applicationContext) {
      ContextAwareEntity.setApplicationContext(entity, applicationContext);
    }
  }
}
