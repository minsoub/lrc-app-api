package com.bithumbsystems.persistence.mongodb.chat.repository;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatMessage;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * The interface Chat message repository.
 */
@Repository
public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, String> {

  /**
   * Find all by chat room and site id and is deleted false flux.
   *
   * @param chatRoom the chat room
   * @param siteId   the site id
   * @return the flux
   */
  Flux<ChatMessage> findAllByChatRoomAndSiteIdAndIsDeleteFalse(String chatRoom, String siteId);

}
