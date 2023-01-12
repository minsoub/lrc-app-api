package com.bithumbsystems.persistence.mongodb.lrcmanagment.user.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.model.enums.UserStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import java.time.LocalDate;

/**
 * The interface User custom repository.
 */
@Repository
public interface UserCustomRepository {
  /**
   * 검색 조건으로 재단 사용자 리스트 조회.
   *
   * @param searchFromDate the search from date
   * @param searchToDate   the search to date
   * @param userStatus     the user status
   * @param keyword        the keyword
   * @return the flux
   */
  Flux<UserInfo> findList(LocalDate searchFromDate, LocalDate searchToDate, UserStatus userStatus, String keyword);
}
