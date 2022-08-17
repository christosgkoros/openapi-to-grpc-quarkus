package org.cgoro.tmf.openapis.tmf620.config;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

@ConfigMapping(prefix = "mongodb.configs")
public interface MongoConfig {

    @WithName("collection")
    String getCollection();

    @WithName("database")
    String getDatabase();
}
