package com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.repository;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity.LineMng;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineMngRepository extends ReactiveMongoRepository<LineMng, String> {
}
