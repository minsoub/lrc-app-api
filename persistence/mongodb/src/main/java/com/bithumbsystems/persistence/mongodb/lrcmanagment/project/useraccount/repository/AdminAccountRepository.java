package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository;

import com.bithumbsystems.persistence.mongodb.account.model.entity.AdminAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminAccountRepository  extends ReactiveMongoRepository<AdminAccount, String> {
}
