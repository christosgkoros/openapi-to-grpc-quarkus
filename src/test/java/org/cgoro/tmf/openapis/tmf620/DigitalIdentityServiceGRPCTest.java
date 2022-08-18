package org.cgoro.tmf.openapis.tmf620;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;

import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import openapitools.DigitalIdentityCreateOuterClass;
import openapitools.DigitalIdentityOuterClass;
import openapitools.services.digitalidentityservice.DigitalIdentityService;
import openapitools.services.digitalidentityservice.DigitalIdentityServiceOuterClass;
import org.cgoro.tmf.openapis.tmf620.db.MongoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
class DigitalIdentityServiceGRPCTest {

    @GrpcClient
    DigitalIdentityService digitalIdentityService;

    @InjectMock
    MongoService mongoService;


    @Test
    void testCreate() {

        Mockito.when(mongoService.createDigitalIdentity(Mockito.any(DigitalIdentityCreateOuterClass.DigitalIdentityCreate.class)))
                .thenReturn(Uni.createFrom().item("12345"));

        Uni<DigitalIdentityOuterClass.DigitalIdentity> identity = digitalIdentityService.createDigitalIdentity(
                DigitalIdentityServiceOuterClass.CreateDigitalIdentityRequest.newBuilder()
                        .setDigitalIdentity(DigitalIdentityCreateOuterClass.DigitalIdentityCreate.newBuilder()
                                .setAtbaseType("DigitalIdentity").build()).build());

        DigitalIdentityOuterClass.DigitalIdentity identityResponse = identity.await().indefinitely();
        assertEquals("12345", identityResponse.getId());
        assertEquals("retrieveDigitalIdentity(12345)", identityResponse.getHref());
    }

}
