package com.bithumbsystems.persistence.mongodb.chat.repository;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ChatChannelRepository extends ReactiveMongoRepository<ChatChannel, String> {

    Mono<ChatChannel> findByAccountId(String accountId);
    Mono<ChatChannel> findByAccountIdAndRoleAndChatRoomsContains(String accountId, ChatRole role, String projectId);

}
