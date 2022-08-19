package org.cgoro.tmf.openapis.tmf720.grpc;

import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import openapitools.DigitalIdentityOuterClass;
import openapitools.services.digitalidentityservice.DigitalIdentityService;
import openapitools.services.digitalidentityservice.DigitalIdentityServiceOuterClass;
import org.cgoro.tmf.openapis.tmf720.db.DigitalIdentityStatus;
import org.cgoro.tmf.openapis.tmf720.db.MongoService;

import javax.inject.Inject;
import java.time.Instant;

@GrpcService
public class DigitalIdentityServiceImpl implements DigitalIdentityService {

    @Inject
    MongoService mongoService;
    @Override
    public Uni<DigitalIdentityOuterClass.DigitalIdentity> createDigitalIdentity(DigitalIdentityServiceOuterClass.CreateDigitalIdentityRequest request) {
        String digitalIdentityCreate;
        try {
            digitalIdentityCreate = JsonFormat.printer().print(request.getDigitalIdentity());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }

        DigitalIdentityOuterClass.DigitalIdentity.Builder builder = DigitalIdentityOuterClass.DigitalIdentity.newBuilder();
        try {
            JsonFormat.parser().merge(digitalIdentityCreate, builder);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
        builder.setCreationDate(Instant.now().toString());
        builder.setStatus(DigitalIdentityStatus.ACTIVE.toString());

        String digitalIdentity = null;
        try {
            digitalIdentity = JsonFormat.printer().print(builder.build());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
        return mongoService.createDigitalIdentity(digitalIdentity)
                .map(id -> {
                    builder.setId(id);
                    builder.setHref("retrieveDigitalIdentity(" + id + ")");
                    return builder.build();
                });
    }

    @Override
    public Uni<Empty> deleteDigitalIdentity(DigitalIdentityServiceOuterClass.DeleteDigitalIdentityRequest request) {
        return null;
    }

    @Override
    public Uni<DigitalIdentityServiceOuterClass.ListDigitalIdentityResponse> listDigitalIdentity(DigitalIdentityServiceOuterClass.ListDigitalIdentityRequest request) {
        return mongoService.getDigitalIdentityList()
                .map(json -> {
                    DigitalIdentityOuterClass.DigitalIdentity.Builder builder = DigitalIdentityOuterClass.DigitalIdentity.newBuilder();
                    try {
                        JsonFormat.parser().merge(json, builder);
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException(e);
                    }
                    builder.setHref("retrieveDigitalIdentity(" + builder.getId() + ")");
                    return builder.build();
                }).collect().asList()
                .map(list -> {
                    DigitalIdentityServiceOuterClass.ListDigitalIdentityResponse.Builder builder = DigitalIdentityServiceOuterClass.ListDigitalIdentityResponse.newBuilder();
                    builder.addAllData(list);
                    return builder.build();
                });
    }

    @Override
    public Uni<DigitalIdentityOuterClass.DigitalIdentity> patchDigitalIdentity(DigitalIdentityServiceOuterClass.PatchDigitalIdentityRequest request) {
        return null;
    }

    @Override
    public Uni<DigitalIdentityOuterClass.DigitalIdentity> retrieveDigitalIdentity(DigitalIdentityServiceOuterClass.RetrieveDigitalIdentityRequest request) {
        return mongoService.retrieveDigitalIdentity(request.getId())
                .map(json -> {
                    DigitalIdentityOuterClass.DigitalIdentity.Builder builder = DigitalIdentityOuterClass.DigitalIdentity.newBuilder();
                    try {
                        JsonFormat.parser().merge(json, builder);
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException(e);
                    }
                    builder.setHref("retrieveDigitalIdentity(" + builder.getId() + ")");
                    return builder.build();
                });
    }
}
