package com.bithumbsystems.persistence.mongodb.servicelog.repository;

import com.bithumbsystems.persistence.mongodb.servicelog.model.ServiceLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceLogRepository extends ReactiveMongoRepository<ServiceLog, String> {
}
