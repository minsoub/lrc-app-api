package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserInfoCustomRepository {
    public Flux<UserInfo> findBySearch(String keyword);
}
