package com.bithumbsystems.persistence.mongodb.lrcmanagment.user.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.model.enums.UserStatus;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * The type User custom repository.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCustomRepositoryImpl implements UserCustomRepository {
  private final ReactiveMongoTemplate reactiveMongoTemplate;

  @Override
  public Flux<UserInfo> findByCustomGetList(LocalDate searchFromDate, LocalDate searchToDate, UserStatus userStatus, String encryptEmail, String keyword) {
    Query query = new Query();
    if (searchFromDate != null && searchToDate != null) {
      query.addCriteria(Criteria.where("create_date").gte(searchFromDate).lte(searchToDate));
    }
    //if (StringUtils.isNotEmpty(keyword)) {
    //  query.addCriteria(new Criteria().orOperator(
    //     Criteria.where("email").is(encryptEmail)
    //      // 생성프로젝트명
    //      // 참여프로젝트명
    //  ));
    //}
    if (userStatus != null) {
      query.addCriteria(Criteria.where("status").is(userStatus));
    }
    return reactiveMongoTemplate.find(query, UserInfo.class);
  }
}
