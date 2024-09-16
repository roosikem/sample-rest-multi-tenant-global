package com.lkup.accounts.accounts.controller;

import com.lkup.accounts.accounts.config.MongoDbTestContainerExtension;
import com.lkup.accounts.accounts.config.RunLast;
import org.junit.jupiter.api.Test;

@RunLast
public class TearDown {

    @Test
    void testLast() {
        MongoDbTestContainerExtension.stopContainer();
        System.out.println("LastTest executed");
    }
}
