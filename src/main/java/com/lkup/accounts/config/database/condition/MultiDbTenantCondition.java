package com.lkup.accounts.config.database.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class MultiDbTenantCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String mode = context.getEnvironment().getProperty("mongodb.tenant.mode");
        return "multi-db".equalsIgnoreCase(mode);
    }
}
