package org.cgoro.tmf.openapis.tmf620.db;

import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.Json;
import openapitools.DigitalIdentityCreateOuterClass;
import org.bson.Document;
import org.cgoro.tmf.openapis.tmf620.config.MongoConfig;
import org.cgoro.tmf.openapis.tmf620.mapper.DigitalIdentityMapper;
import org.openapitools.model.DigitalIdentity;

import javax.enterprise.context.ApplicationScoped;
import java.sql.Date;
import java.time.Clock;
import java.time.Instant;

@ApplicationScoped
public class MongoService {

    private final ReactiveMongoClient mongoClient;
    private final MongoConfig mongoConfig;

    public MongoService(ReactiveMongoClient mongoClient,
                        MongoConfig mongoConfiguration) {
        this.mongoClient = mongoClient;
        this.mongoConfig = mongoConfiguration;
    }

    public Uni<String> createDigitalIdentity(DigitalIdentityCreateOuterClass.DigitalIdentityCreate digitalIdentity) {

        DigitalIdentity digitalIdentityJSONModel = DigitalIdentityMapper.INSTANCE.grpcToJsonModel(digitalIdentity);
        digitalIdentityJSONModel.setStatus(DigitalIdentityStatus.ACTIVE.toString());
        digitalIdentityJSONModel.setCreationDate(Date.from(Instant.now(Clock.systemUTC())));


        return mongoClient.getDatabase(mongoConfig.getDatabase())
                .getCollection(mongoConfig.getCollection())
                .insertOne(Document.parse(Json.encode(digitalIdentityJSONModel)))
                .map(result -> result.getInsertedId().asObjectId().getValue().toString());
    }
}
