package com.bithumbsystems.persistence.mongodb.account.service;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AccountDomainService {

     private final AdminAccountRepository adminAccountRepository;

    public Mono<AdminAccount> findByAdminId(String id) {
        return adminAccountRepository.findById(id);
    }
}
