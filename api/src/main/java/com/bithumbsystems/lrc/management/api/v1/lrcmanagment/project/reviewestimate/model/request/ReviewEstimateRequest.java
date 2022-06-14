package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.ReviewEstimate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEstimateRequest {

    List<ReviewEstimate> reviewEstimateList;
}
