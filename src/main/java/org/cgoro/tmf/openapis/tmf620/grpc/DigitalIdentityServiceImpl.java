package org.cgoro.tmf.openapis.tmf620.grpc;

import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import openapitools.DigitalIdentityOuterClass;
import openapitools.services.digitalidentityservice.DigitalIdentityService;
import openapitools.services.digitalidentityservice.DigitalIdentityServiceOuterClass;
import org.cgoro.tmf.openapis.tmf620.db.MongoService;

import javax.inject.Inject;

@GrpcService
public class DigitalIdentityServiceImpl implements DigitalIdentityService {

    @Inject
    MongoService mongoService;
    @Override
    public Uni<DigitalIdentityOuterClass.DigitalIdentity> createDigitalIdentity(DigitalIdentityServiceOuterClass.CreateDigitalIdentityRequest request) {
        Uni<String> id = mongoService.createDigitalIdentity(request.getDigitalIdentity());
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
        return null;
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
