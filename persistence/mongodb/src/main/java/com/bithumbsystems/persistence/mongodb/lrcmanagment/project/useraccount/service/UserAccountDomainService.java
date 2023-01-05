package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserAccount;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserAccountCustomRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type User account domain service.
 */
@Service
@RequiredArgsConstructor
public class UserAccountDomainService {

  private final UserAccountRepository userAccountRepository;
  private final UserAccountCustomRepository userAccountCustomRepository;

  /**
   * 담당자 정보 project id로 찾기.
   *
   * @param projectId the project id
   * @return UserAccountResponse Object
   */
  public Flux<UserAccount> findByProjectId(String projectId) {
    return userAccountRepository.findByProjectId(projectId);
  }

  /**
   * 담당자 정보 여러개 저장 및 업데이트.
   *
   * @param userAccount the user account
   * @return userAccountResponse Object
   */
  public Mono<UserAccount> save(UserAccount userAccount) {
    return userAccountRepository.save(userAccount);
  }

  /**
   * 담당자 정보 삭제.
   *
   * @param userAccount the user account
   * @return mono
   */
  public Mono<Void> delete(UserAccount userAccount) {
    return userAccountRepository.delete(userAccount);
  }

  /**
   * 담당자 ID로 정보 검색.
   *
   * @param id the id
   * @return mono
   */
  public Mono<UserAccount> findById(String id) {
    return userAccountRepository.findById(id);
  }

  /**
   * 프로젝트 ID와 거래지원 사용자 계정 ID로 찾는다.
   *
   * @param projectId     the project id
   * @param userAccountId the user account id
   * @return mono
   */
  public Mono<UserAccount> findByProjectIdAndUserAccountId(String projectId, String userAccountId) {
    return userAccountRepository.findByProjectIdAndUserAccountId(projectId, userAccountId);
  }

  /**
   * 프로젝트 ID와 이메일로 검색
   *
   * @param projectId     the project id
   * @param contactEmail the user contact email
   * @return mono
   */
  public Mono<UserAccount> findByProjectIdAndContactEmail(String projectId, String contactEmail) {
    return userAccountRepository.findByProjectIdAndContactEmail(projectId, contactEmail);
  }

  /**
   * 이메일 정보로 참여 프로젝트 리스트 조회.
   *
   * @param email the email
   * @return the flux
   */
  public Flux<UserAccount> findByCustomProjectInfo(String email) {
    return userAccountCustomRepository.findByCustomProjectInfo(email);
  }
}
