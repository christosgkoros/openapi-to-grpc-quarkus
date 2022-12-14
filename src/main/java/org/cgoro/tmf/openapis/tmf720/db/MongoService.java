package org.cgoro.tmf.openapis.tmf720.db;

import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.cgoro.tmf.openapis.tmf720.config.MongoConfig;

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

    public Multi<String> getDigitalIdentityList() {
        return mongoClient.getDatabase(mongoConfig.getDatabase())
                .getCollection(mongoConfig.getCollection())
                .find()
                .map(document -> {
                    document.append("id",document.get("_id").toString());
                    document.remove("_id");
                    return  document.toJson();
                });
    }

    public Uni<String> retrieveDigitalIdentity(String id) {
        return mongoClient.getDatabase(mongoConfig.getDatabase())
                .getCollection(mongoConfig.getCollection())
                .find(new Document("_id", new ObjectId(id)))
                .toUni()
                .map(document -> {
                    document.append("id",document.get("_id").toString());
                    document.remove("_id");
                    return  document.toJson();
                });
    }

    public void deleteDigitalIdentity(String id) {
        mongoClient.getDatabase(mongoConfig.getDatabase())
                .getCollection(mongoConfig.getCollection())
                .deleteOne(new Document("_id", id));
    }

    public Uni<String> updateDigitalIdentity(String id, String identity) {
        return mongoClient.getDatabase(mongoConfig.getDatabase())
                .getCollection(mongoConfig.getCollection())
                .findOneAndUpdate(new Document("_id", new ObjectId(id)), new Document("$set", Document.parse(identity)),
                        new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER))
                .map(document -> {
                    document.append("id",document.get("_id").toString());
                    document.remove("_id");
                    return  document.toJson();
                });
    }
}
