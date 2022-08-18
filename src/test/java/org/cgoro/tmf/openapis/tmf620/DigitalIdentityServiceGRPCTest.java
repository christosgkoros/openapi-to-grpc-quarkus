package org.cgoro.tmf.openapis.tmf620;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;

import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Multi;
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

        Mockito.when(mongoService.createDigitalIdentity(Mockito.any(String.class)))
                .thenReturn(Uni.createFrom().item("12345"));

        DigitalIdentityOuterClass.DigitalIdentity identityResponse = digitalIdentityService.createDigitalIdentity(
                        DigitalIdentityServiceOuterClass.CreateDigitalIdentityRequest.newBuilder()
                                .setDigitalIdentity(DigitalIdentityCreateOuterClass.DigitalIdentityCreate.newBuilder()
                                        .setAtbaseType("DigitalIdentity").build()).build())
                .await().indefinitely();

        assertEquals("12345", identityResponse.getId());
        assertEquals("retrieveDigitalIdentity(12345)", identityResponse.getHref());
        assertEquals("ACTIVE", identityResponse.getStatus());
    }

    @Test
    public void testList() {

        Mockito.when(mongoService.getDigitalIdentityList())
                .thenReturn(Multi.createFrom().items(
                        "{\"id\":\"12345\",\"href\":\"retrieveDigitalIdentity(12345)\",\"status\":\"ACTIVE\"}",
                        "{\"id\":\"67890\",\"href\":\"retrieveDigitalIdentity(67890)\",\"status\":\"ACTIVE\"}",
                        "{\"id\":\"abcde\",\"href\":\"retrieveDigitalIdentity(abcde)\",\"status\":\"ACTIVE\"}")
                );

        DigitalIdentityServiceOuterClass.ListDigitalIdentityResponse response = digitalIdentityService.listDigitalIdentity(
                DigitalIdentityServiceOuterClass.ListDigitalIdentityRequest.newBuilder().build()).await().indefinitely();

        assertEquals(3, response.getDataList().size());
        assertEquals("12345", response.getDataList().get(0).getId());
        assertEquals("67890", response.getDataList().get(1).getId());
        assertEquals("abcde", response.getDataList().get(2).getId());

    }

    @Test
    void retrieveDigitalIdentity() {

        Mockito.when(mongoService.retrieveDigitalIdentity(Mockito.any(String.class)))
                .thenReturn(Uni.createFrom().item("{\"id\":\"12345\",\"href\":\"retrieveDigitalIdentity(12345)\",\"status\":\"ACTIVE\"}"));

        DigitalIdentityOuterClass.DigitalIdentity response = digitalIdentityService.retrieveDigitalIdentity(DigitalIdentityServiceOuterClass.RetrieveDigitalIdentityRequest.newBuilder()
                .setId("12345").build()).await().indefinitely();

        assertEquals("12345", response.getId());
        assertEquals("retrieveDigitalIdentity(12345)", response.getHref());
    }
}
