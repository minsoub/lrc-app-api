package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import com.bithumbsystems.persistence.mongodb.account.repository.AdminAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AdminAccountDomainService {
    private final AdminAccountRepository adminAccountRepository;
    /**
     * Find by admin account id mono.
     *
     * @param adminAccountId the admin account id
     * @return the mono
     */
    public Mono<AdminAccount> findByAdminAccountId(String adminAccountId) {
        return adminAccountRepository.findById(adminAccountId);
    }
}
