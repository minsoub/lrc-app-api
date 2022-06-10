package com.bithumbsystems.persistence.mongodb.statusmanagement.networklist.repository;

import com.bithumbsystems.persistence.mongodb.statusmanagement.networklist.model.entity.NetworkList;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetworkListRepository extends ReactiveMongoRepository<NetworkList, String> {
}
