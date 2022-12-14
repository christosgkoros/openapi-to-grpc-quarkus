package org.cgoro.tmf.openapis.tmf720;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcClient;
import io.quarkus.test.junit.QuarkusTest;

import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import openapitools.DigitalIdentityCreateOuterClass;
import openapitools.DigitalIdentityOuterClass;
import openapitools.DigitalIdentityUpdateOuterClass;
import openapitools.services.digitalidentityservice.DigitalIdentityService;
import openapitools.services.digitalidentityservice.DigitalIdentityServiceOuterClass;
import org.cgoro.tmf.openapis.tmf720.db.DigitalIdentityStatus;
import org.cgoro.tmf.openapis.tmf720.db.MongoService;
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
    void testList() {

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
    void testRetrieve() {

        Mockito.when(mongoService.retrieveDigitalIdentity(Mockito.any(String.class)))
                .thenReturn(Uni.createFrom().item("{\"id\":\"12345\",\"href\":\"retrieveDigitalIdentity(12345)\",\"status\":\"ACTIVE\"}"));

        DigitalIdentityOuterClass.DigitalIdentity response = digitalIdentityService.retrieveDigitalIdentity(DigitalIdentityServiceOuterClass.RetrieveDigitalIdentityRequest.newBuilder()
                .setId("12345").build()).await().indefinitely();

        assertEquals("12345", response.getId());
        assertEquals("retrieveDigitalIdentity(12345)", response.getHref());
    }

    @Test
    void testRetrieveNotFound() {
        //TODO: implement
    }
    @Test
    void testDelete() {
        Empty empty = digitalIdentityService.deleteDigitalIdentity(DigitalIdentityServiceOuterClass.DeleteDigitalIdentityRequest.newBuilder()
                .setId("12345").build()).await().indefinitely();

        Mockito.verify(mongoService).deleteDigitalIdentity("12345");
    }

    @Test
    void testDeleteNotFound() {
        //TODO: implement
    }

    @Test
    void testUpdate() {

        Mockito.when(mongoService.updateDigitalIdentity(Mockito.any(String.class), Mockito.any(String.class)))
                .thenReturn(Uni.createFrom().item("{\"id\":\"12345\",\"href\":\"retrieveDigitalIdentity(12345)\",\"status\":\"INACTIVE\"}"));

        DigitalIdentityOuterClass.DigitalIdentity identity = digitalIdentityService.patchDigitalIdentity(DigitalIdentityServiceOuterClass.PatchDigitalIdentityRequest.newBuilder()
                .setId("12345")
                .setDigitalIdentity(DigitalIdentityUpdateOuterClass.DigitalIdentityUpdate.newBuilder()
                        .setStatus(DigitalIdentityStatus.INACTIVE.name()).build())
                .build()).await().indefinitely();

        assertEquals("12345", identity.getId());
        assertEquals("retrieveDigitalIdentity(12345)", identity.getHref());
        assertEquals(identity.getStatus(), DigitalIdentityStatus.INACTIVE.name());
    }

    @Test
    void testUpdateNotFound() {

    }
}
