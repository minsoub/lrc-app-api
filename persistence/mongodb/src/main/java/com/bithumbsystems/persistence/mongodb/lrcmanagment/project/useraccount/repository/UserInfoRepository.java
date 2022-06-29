package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository  extends ReactiveMongoRepository<UserInfo, String> {
}
