package chess.util.persistence;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HibernatePropertiesCustomizer implements org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer {

    @Autowired
    private EntityInterceptor entityInterceptor;

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", entityInterceptor);
    }
}
