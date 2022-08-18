package org.cgoro.tmf.openapis.tmf620.db;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import org.bson.Document;
import org.cgoro.tmf.openapis.tmf620.config.MongoConfig;
import org.openapitools.model.DigitalIdentity;

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

    public Uni<String> createDigitalIdentity(DigitalIdentity digitalIdentity) {

       return mongoClient.getDatabase(mongoConfig.getDatabase())
                .getCollection(mongoConfig.getCollection())
                .insertOne(Document.parse(Json.encode(digitalIdentity)))
                .map(result -> result.getInsertedId().asObjectId().getValue().toString());
    }

    public Multi<DigitalIdentity> listDigitaIdentity() {
        return null;
    }
}
