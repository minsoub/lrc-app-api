package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserAccount;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserAccountDomainService {

    private final UserAccountRepository userAccountRepository;

    /**
     * 담당자 정보 id로 찾기
     * @param projectId
     * @return UserAccountResponse Object
     */
    public Flux<UserAccount> findByProjectId(String projectId) {
        return userAccountRepository.findByProjectId(projectId);
    }

    /**
     * 담당자 정보 여러개 저장 및 업데이트
     * @param userAccount
     * @return userAccountResponse Object
     */
    public Mono<UserAccount> save(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }
}
