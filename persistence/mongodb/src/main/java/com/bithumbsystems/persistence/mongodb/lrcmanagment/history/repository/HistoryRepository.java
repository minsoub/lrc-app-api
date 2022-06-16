package com.bithumbsystems.persistence.mongodb.lrcmanagment.history.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.History;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface HistoryRepository extends ReactiveMongoRepository<History, String> {
}
