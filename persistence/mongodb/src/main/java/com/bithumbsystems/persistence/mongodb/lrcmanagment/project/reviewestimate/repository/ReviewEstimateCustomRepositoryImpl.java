package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.entity.ReviewEstimate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewEstimateCustomRepositoryImpl implements ReviewEstimateCustomRepository{

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<ReviewEstimate> findByUseData(String projectId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("project_id").is(projectId));
        query.addCriteria(new Criteria().orOperator(
                Criteria.where("del_yn").is(false),
                Criteria.where("del_yn").is(null)
        ));

        return reactiveMongoTemplate.find(query, ReviewEstimate.class);
    }
}
