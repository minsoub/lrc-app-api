package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * 이메일 정보로 참여 프로젝트 리스트 조회.
 */
@RequiredArgsConstructor
@Repository
public class UserAccountCustomRepositoryImpl implements UserAccountCustomRepository {
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<UserAccount> findByCustomProjectInfo(String email) {
    MatchOperation matchOperation = Aggregation.match(Criteria.where("contact_email").is(email));
    LookupOperation lookupOperation = Aggregation.lookup("lrc_project_foundation_info", "project_id", "_id", "foundation_info");
    UnwindOperation unwindOperation = Aggregation.unwind("foundation_info", true);
    Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupOperation, unwindOperation);

    return reactiveMongoTemplate.aggregate(aggregation, "lrc_project_user_account", UserAccount.class);
  }
}
