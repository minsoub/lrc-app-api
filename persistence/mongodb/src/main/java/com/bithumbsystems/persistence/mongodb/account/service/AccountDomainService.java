package com.bithumbsystems.persistence.mongodb.account.service;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * The type Account domain service.
 */
@Service
@RequiredArgsConstructor
public class AccountDomainService {
  private final AdminAccountRepository adminAccountRepository;

  /**
   * Find by admin id mono.
   *
   * @param id the id
   * @return the mono
   */
  public Mono<AdminAccount> findByAdminId(String id) {
    return adminAccountRepository.findById(id);
  }
}
