package com.bithumbsystems.persistence.mongodb.chat.repository;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatFile;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ChatFileRepository extends ReactiveMongoRepository<ChatFile, String> {
    public Flux<ChatFile> findByProjectId(String projectId);
}
