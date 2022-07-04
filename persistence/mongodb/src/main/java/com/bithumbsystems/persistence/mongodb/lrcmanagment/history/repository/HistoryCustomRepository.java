package com.bithumbsystems.persistence.mongodb.lrcmanagment.history.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface HistoryCustomRepository {
    public Flux<History> findBySearch(String projectId, String keyword);
}
