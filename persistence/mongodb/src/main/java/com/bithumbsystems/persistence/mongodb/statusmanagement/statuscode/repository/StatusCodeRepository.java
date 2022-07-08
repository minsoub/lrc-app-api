package com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.repository;

import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.model.entity.StatusCode;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StatusCodeRepository extends ReactiveMongoRepository<StatusCode, String> {

    Flux<StatusCode> findByParentCode(String parentCode);


}
