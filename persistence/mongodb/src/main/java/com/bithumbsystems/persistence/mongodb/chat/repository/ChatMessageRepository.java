package com.bithumbsystems.persistence.mongodb.chat.repository;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {

    public Flux<ChatMessage> findByChatRoomAndSiteId(String chatRoom, String siteId);
}
