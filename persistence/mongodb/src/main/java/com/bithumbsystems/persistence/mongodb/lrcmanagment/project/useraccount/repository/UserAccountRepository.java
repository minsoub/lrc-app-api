package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserAccountRepository extends ReactiveMongoRepository<UserAccount, String> {

    Flux<UserAccount> findByProjectId(String projectId);

    Mono<UserAccount> findByProjectIdAndUserAccountId(String projectId, String userAccountId);

    Mono<UserAccount> findByProjectIdAndContactEmail(String projectId, String contactEmail);
}
