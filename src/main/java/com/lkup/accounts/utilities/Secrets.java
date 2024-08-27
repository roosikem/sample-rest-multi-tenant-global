package com.lkup.accounts.utilities;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class Secrets {
    private final Environment environment;

    public Secrets(Environment environment) {
        this.environment = environment;
    }
}
