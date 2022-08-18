package org.cgoro.tmf.openapis.tmf620.mapper;

import openapitools.DigitalIdentityCreateOuterClass;
import openapitools.DigitalIdentityOuterClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.openapitools.model.DigitalIdentity;

@Mapper(uses = TypeMapper.class)
public interface DigitalIdentityMapper {

    DigitalIdentityMapper INSTANCE = Mappers.getMapper(DigitalIdentityMapper.class);

    @Mapping(target = "atType", source = "attype")
    @Mapping(target = "atBaseType", source = "atbaseType")
    @Mapping(target = "atSchemaLocation", source = "atschemaLocation")
    @Mapping(target = "attachment", source = "attachmentList")
    @Mapping(target = "contactMedium", source = "contactMediumList")
    @Mapping(target = "credential", source = "credentialList")
    @Mapping(target = "partyRoleIdentified", source = "partyRoleIdentifiedList")
    @Mapping(target = "relatedParty", source = "relatedPartyList")
    DigitalIdentity grpcToJsonModel(DigitalIdentityCreateOuterClass.DigitalIdentityCreate grpciIdentity);

    @Mapping(target = "attype", source = "atType")
    @Mapping(target = "atbaseType", source = "atBaseType")
    @Mapping(target = "atschemaLocation", source = "atSchemaLocation")
    @Mapping(target = "attachmentList", source = "attachment")
    @Mapping(target = "contactMediumList", source = "contactMedium")
    @Mapping(target = "credentialList", source = "credential")
    @Mapping(target = "partyRoleIdentifiedList", source = "partyRoleIdentified")
    @Mapping(target = "relatedPartyList", source = "relatedParty")
    DigitalIdentityOuterClass.DigitalIdentity jsonModelToGrpc(DigitalIdentity jsonIdentity);
}
