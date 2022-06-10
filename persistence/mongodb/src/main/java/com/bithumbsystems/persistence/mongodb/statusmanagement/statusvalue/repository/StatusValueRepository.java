package com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.repository;

import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity.StatusValueList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface StatusValueRepository extends ReactiveMongoRepository<StatusValueList, String> {

    Flux<StatusValueList> findByParentCode(String parentCode);
}
