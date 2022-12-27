package com.bithumbsystems.persistence.mongodb.chat.repository;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * The interface Chat channel repository.
 */
@Repository
public interface ChatChannelRepository extends ReactiveMongoRepository<ChatChannel, String> {

  /**
   * Find by account id mono.
   *
   * @param accountId the account id
   * @return the mono
   */
  Mono<ChatChannel> findByAccountId(String accountId);

  /**
   * Find by account id and role and chat rooms contains mono.
   *
   * @param accountId the account id
   * @param role      the role
   * @param projectId the project id
   * @return the mono
   */
  Mono<ChatChannel> findByAccountIdAndRoleAndChatRoomsContains(String accountId, ChatRole role, String projectId);

}
