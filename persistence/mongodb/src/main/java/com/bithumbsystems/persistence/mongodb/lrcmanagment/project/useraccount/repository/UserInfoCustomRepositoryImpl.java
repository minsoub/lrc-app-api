package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserInfoCustomRepositoryImpl implements UserInfoCustomRepository{

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<UserInfo> findBySearch(String keyword) {
        Query query = new Query();

            query.addCriteria(
                    Criteria.where("email").regex(".*" + keyword + ".*", "i")
            );

        return reactiveMongoTemplate.find(query, UserInfo.class);
    }
}
