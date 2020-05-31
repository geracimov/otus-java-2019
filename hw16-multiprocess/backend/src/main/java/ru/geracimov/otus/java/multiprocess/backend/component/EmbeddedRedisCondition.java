package ru.geracimov.otus.java.multiprocess.backend.component;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

public class EmbeddedRedisCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        final String host = context.getEnvironment().getProperty("redis.host");
        return (host == null);
    }

}
