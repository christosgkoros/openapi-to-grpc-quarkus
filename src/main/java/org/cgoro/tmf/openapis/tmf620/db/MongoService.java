package org.cgoro.tmf.openapis.tmf620.db;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.smallrye.mutiny.Uni;
import org.bson.Document;
import org.cgoro.tmf.openapis.tmf620.config.MongoConfig;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MongoService {

    private final ReactiveMongoClient mongoClient;
    private final MongoConfig mongoConfig;

    public MongoService(ReactiveMongoClient mongoClient,
                        MongoConfig mongoConfiguration) {
        this.mongoClient = mongoClient;
        this.mongoConfig = mongoConfiguration;
    }

    public Uni<String> createDigitalIdentity(String digitalIdentity) {
        return mongoClient.getDatabase(mongoConfig.getDatabase())
                .getCollection(mongoConfig.getCollection())
                .insertOne(Document.parse(digitalIdentity))
                .map(result -> result.getInsertedId().asObjectId().getValue().toString());
    }
}
