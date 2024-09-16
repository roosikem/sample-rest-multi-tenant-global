package com.lkup.accounts.accounts.config;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.utility.DockerImageName;

public class MongoDbTestContainerExtension implements BeforeAllCallback, AfterAllCallback {

    // Make the container static so it is shared across all test classes
    private static MongoDBContainer mongoDBContainer;

    @Override
    public void beforeAll(ExtensionContext context) {
        if (mongoDBContainer == null) {
            // Initialize and start the container once
            mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.4.2"));
            mongoDBContainer.start();
            System.out.println("MongoDB container started.");
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        // Do nothing here, we'll stop the container explicitly after all tests have finished
    }

    public static void stopContainer() {
        if (mongoDBContainer != null && mongoDBContainer.isRunning()) {
            mongoDBContainer.stop();
            System.out.println("MongoDB container stopped.");
        }
    }

    public static String getReplicaSetUrl() {
        return mongoDBContainer.getReplicaSetUrl();
    }
}



