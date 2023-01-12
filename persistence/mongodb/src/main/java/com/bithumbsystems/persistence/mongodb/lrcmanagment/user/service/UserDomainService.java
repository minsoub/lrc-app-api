package com.bithumbsystems.persistence.mongodb.lrcmanagment.user.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.model.enums.UserStatus;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.repository.UserCustomRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

/**
 * The type User domain service.
 */
@Service
@RequiredArgsConstructor
public class UserDomainService {
  private final UserCustomRepository userCustomRepository;

  /**
   * 검색 조건으로 재단 사용자 리스트 조회.
   *
   * @param searchFromDate the search from date
   * @param searchToDate   the search to date
   * @param userStatus     the user status
   * @param keyword        the keyword
   * @return the flux
   */
  public Flux<UserInfo> findList(
      LocalDate searchFromDate, LocalDate searchToDate, UserStatus userStatus, String keyword) {
    return userCustomRepository.findList(searchFromDate, searchToDate, userStatus, keyword);
  }
}
