package com.bithumbsystems.persistence.mongodb.statusmanagement.businesslist.repository;

import com.bithumbsystems.persistence.mongodb.statusmanagement.businesslist.model.entity.BusinessList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends ReactiveMongoRepository<BusinessList, String> {
}
