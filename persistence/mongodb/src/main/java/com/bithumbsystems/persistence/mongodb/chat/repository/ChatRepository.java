package com.bithumbsystems.persistence.mongodb.chat.repository;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatChannel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChatRepository extends ReactiveMongoRepository<ChatChannel, String> {

    public Mono<ChatChannel> findByAccountId(String accountId);
}
