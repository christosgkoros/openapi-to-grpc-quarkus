package org.cgoro.tmf.openapis.tmf620.grpc;

import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import openapitools.DigitalIdentityOuterClass;
import openapitools.services.digitalidentityservice.DigitalIdentityService;
import openapitools.services.digitalidentityservice.DigitalIdentityServiceOuterClass;
import org.cgoro.tmf.openapis.tmf620.db.DigitalIdentityStatus;
import org.cgoro.tmf.openapis.tmf620.db.MongoService;
import org.cgoro.tmf.openapis.tmf620.mapper.DigitalIdentityMapper;
import org.openapitools.model.DigitalIdentity;

import javax.inject.Inject;
import java.time.Clock;
import java.time.Instant;
import java.util.Date;

@GrpcService
public class DigitalIdentityServiceImpl implements DigitalIdentityService {

    @Inject
    MongoService mongoService;
    @Override
    public Uni<DigitalIdentityOuterClass.DigitalIdentity> createDigitalIdentity(DigitalIdentityServiceOuterClass.CreateDigitalIdentityRequest request) {

        DigitalIdentity digitalIdentityJSONModel = DigitalIdentityMapper.INSTANCE.grpcToJsonModel(request.getDigitalIdentity());
        digitalIdentityJSONModel.setStatus(DigitalIdentityStatus.ACTIVE.toString());
        digitalIdentityJSONModel.setCreationDate(Date.from(Instant.now(Clock.systemUTC())));

        Uni<String> id = mongoService.createDigitalIdentity(digitalIdentityJSONModel);
        return id.map(value -> DigitalIdentityOuterClass.DigitalIdentity.newBuilder()
                .setId(value)
                .setHref("retrieveDigitalIdentity("+value+")").build());
    }

    @Override
    public Uni<Empty> deleteDigitalIdentity(DigitalIdentityServiceOuterClass.DeleteDigitalIdentityRequest request) {
        return null;
    }

    @Override
    public Uni<DigitalIdentityServiceOuterClass.ListDigitalIdentityResponse> listDigitalIdentity(DigitalIdentityServiceOuterClass.ListDigitalIdentityRequest request) {

        return mongoService.listDigitaIdentity()
                .map(value -> DigitalIdentityMapper.INSTANCE.jsonModelToGrpc(value))
                .collect().asList().map(value -> DigitalIdentityServiceOuterClass.ListDigitalIdentityResponse.newBuilder().addAllData(value).build());
    }

    @Override
    public Uni<DigitalIdentityOuterClass.DigitalIdentity> patchDigitalIdentity(DigitalIdentityServiceOuterClass.PatchDigitalIdentityRequest request) {
        return null;
    }

    @Override
    public Uni<DigitalIdentityOuterClass.DigitalIdentity> retrieveDigitalIdentity(DigitalIdentityServiceOuterClass.RetrieveDigitalIdentityRequest request) {
        return null;
    }
}
