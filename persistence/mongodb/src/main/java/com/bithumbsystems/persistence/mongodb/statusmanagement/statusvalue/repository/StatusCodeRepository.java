package com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.repository;

import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity.StatusCode;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StatusCodeRepository extends ReactiveMongoRepository<StatusCode, String> {

    Flux<StatusCode> findByParentCode(String parentCode);


}
