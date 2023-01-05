package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserAccount;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * The interface User account custom repository.
 */
@Repository
public interface UserAccountCustomRepository {
  /**
   * 이메일 정보로 참여 프로젝트 리스트 조회.
   *
   * @param email the email
   * @return the flux
   */
  Flux<UserAccount> findByCustomProjectInfo(String email);
}
