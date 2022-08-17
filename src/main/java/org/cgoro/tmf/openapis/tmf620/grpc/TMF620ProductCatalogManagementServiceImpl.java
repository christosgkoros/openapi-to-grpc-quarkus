package org.cgoro.tmf.openapis.tmf620.grpc;

import io.quarkus.grpc.GrpcService;
import org.cgoro.tmf.openapis.tmf620.db.MongoService;
import tmf.openapis.TMF620ProductCatalogManagementService;
import io.smallrye.mutiny.Uni;
import tmf.openapis.Tmf620V4;

import javax.inject.Inject;


@GrpcService
public class TMF620ProductCatalogManagementServiceImpl implements TMF620ProductCatalogManagementService{

    @Inject
    MongoService mongoService;

    @Override
    public Uni<Tmf620V4.ProductOffering> listProductOffering(Tmf620V4.ListProductOfferingRequest request) {
        return Uni.createFrom().item(
                Tmf620V4.ProductOffering.newBuilder().setId("1").setName("ProductOffering1").build());
    }

    @Override
    public Uni<Tmf620V4.ProductOffering> createProductOffering(Tmf620V4.CreateProductOfferingRequest request) {
        return mongoService.createProductOffering(request.getProductOffering());
    }

    @Override
    public Uni<Tmf620V4.ProductOffering> retrieveProductOffering(Tmf620V4.RetrieveProductOfferingRequest request) {
        return null;
    }

    @Override
    public Uni<Tmf620V4.Error> deleteProductOffering(Tmf620V4.DeleteProductOfferingRequest request) {
        return null;
    }

    @Override
    public Uni<Tmf620V4.ProductOffering> patchProductOffering(Tmf620V4.PatchProductOfferingRequest request) {
        return null;
    }
}
