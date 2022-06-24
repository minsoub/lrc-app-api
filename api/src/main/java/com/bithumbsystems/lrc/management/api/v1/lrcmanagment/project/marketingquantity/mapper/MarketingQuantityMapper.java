package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.request.MarketingQuantityRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.response.MarketingQuantityResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.model.entity.MarketingQuantity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MarketingQuantityMapper {

    MarketingQuantityMapper INSTANCE = Mappers.getMapper(MarketingQuantityMapper.class);

    MarketingQuantityResponse marketingQuantityResponse(MarketingQuantity marketingQuantity);

    MarketingQuantity marketingQuantityResponseToMarketingQuantity(MarketingQuantityRequest.Marketing marketingRequest);
}
