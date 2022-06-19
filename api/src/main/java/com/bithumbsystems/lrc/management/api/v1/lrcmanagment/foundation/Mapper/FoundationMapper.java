package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.Mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.request.FoundationRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.FoundationResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.model.entity.Foundation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FoundationMapper {

    FoundationMapper INSTANCE = Mappers.getMapper(FoundationMapper.class);

    FoundationResponse foundationResponse(Foundation foundation);

    Foundation foundationRequestToFoundation(FoundationRequest foundationRequest);
}
