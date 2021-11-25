package chess.user.model;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

@Component
public class UserPropertiesCustomizer implements HibernatePropertiesCustomizer {

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", userInterceptor);
    }
}
