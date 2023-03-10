package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request.ReviewEstimateRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.response.ReviewEstimateResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.entity.ReviewEstimate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ReviewEstimateMapper {

    ReviewEstimateMapper INSTANCE = Mappers.getMapper(ReviewEstimateMapper.class);

    ReviewEstimateResponse reviewEstimateResponse(ReviewEstimate reviewEstimate);

    ReviewEstimate reviewEstimateResponseToRequest(ReviewEstimate reviewEstimate);

    //ReviewEstimate requestToReviewEstimate(ReviewEstimateRequest reviewEstimate);

}
