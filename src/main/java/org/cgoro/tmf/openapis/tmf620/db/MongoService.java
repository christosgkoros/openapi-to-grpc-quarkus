package org.cgoro.tmf.openapis.tmf620.db;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import org.bson.Document;
import org.cgoro.tmf.openapis.tmf620.config.MongoConfig;
import tmf.openapis.Tmf620V4;

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

    public ReactiveMongoClient getMongoClient() {
        return mongoClient;
    }

    //insert productoffering to collection
    public Uni<Tmf620V4.ProductOffering> createProductOffering(Tmf620V4.ProductOfferingCreate productOffering) {
        return mongoClient.getDatabase(mongoConfig.getDatabase()).getCollection(mongoConfig.getCollection())
                .insertOne(Document.parse(Json.encode(productOffering)))
                .map(result -> {
                    String _id = result.getInsertedId().asObjectId().getValue().toString();
                    return Tmf620V4.ProductOffering.newBuilder().setId(_id).build();
                });
    }

}
