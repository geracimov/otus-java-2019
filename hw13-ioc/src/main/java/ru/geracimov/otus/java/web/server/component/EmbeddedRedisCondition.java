package ru.geracimov.otus.java.web.server.component;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class EmbeddedRedisCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        final String host = context.getEnvironment().getProperty("redis.host");
        return (host == null);
    }

}
