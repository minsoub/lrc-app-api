package com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.repository;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity.LineMng;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * The type Line mng custom repository.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class LineMngCustomRepositoryImpl implements LineMngCustomRepository {
  private final ReactiveMongoTemplate reactiveMongoTemplate;
  @Override
  public Mono<LineMng> findByIdWithParentInfo(String id) {
    MatchOperation matchOperation = Aggregation.match(Criteria.where("_id").is(id));
    LookupOperation lookupOperation = Aggregation.lookup("lrc_line_mng", "parent_id", "_id", "parent_info");
    UnwindOperation unwindOperation = Aggregation.unwind("parent_info", true);
    Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupOperation, unwindOperation);

    return reactiveMongoTemplate.aggregate(aggregation, "lrc_line_mng", LineMng.class).next();
  }
}
