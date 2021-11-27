package chess.util.persistence;

import javax.persistence.Transient;

import org.springframework.context.ApplicationContext;

public class ContextAwareEntity {
  protected ApplicationContext springApplicationContext;

  private void setApplicationContext(ApplicationContext ctx) {
    springApplicationContext = ctx;
  }

  protected static void setApplicationContext(ContextAwareEntity entity, ApplicationContext applicationContext) {
    entity.setApplicationContext(applicationContext);
  }

  @Transient
  protected ApplicationContext applicationContext;
}
