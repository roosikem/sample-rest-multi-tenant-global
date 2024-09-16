package com.lkup.accounts.accounts.config;



import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.ClassOrdererContext;

import java.util.Comparator;

public class LastTestClassOrderer implements ClassOrderer {

    @Override
    public void orderClasses(ClassOrdererContext context) {
        context.getClassDescriptors().sort(Comparator.comparingInt(descriptor -> {
            if (descriptor.getTestClass().isAnnotationPresent(RunLast.class)) {
                return 1;  // Assign higher order to classes annotated with @RunLast
            }
            return 0;  // Assign lower order to all other classes
        }));
    }
}

