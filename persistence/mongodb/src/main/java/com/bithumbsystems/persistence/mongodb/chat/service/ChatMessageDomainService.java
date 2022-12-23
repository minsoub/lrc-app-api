package com.bithumbsystems.persistence.mongodb.chat.service;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.chat.repository.ChatMessageRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Chat message domain service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageDomainService {

  private final ChatMessageRepository chatMessageRepository;

  /**
   * Save mono.
   *
   * @param chatMessage the chat message
   * @return the mono
   */
  public Mono<ChatMessage> save(ChatMessage chatMessage) {
    chatMessage.setCreateDate(LocalDateTime.now());
    chatMessage.setId(UUID.randomUUID().toString());
    return chatMessageRepository.save(chatMessage);
  }

  /**
   * Find messages flux.
   *
   * @param chatRoom the chat room
   * @param siteId   the site id
   * @return the flux
   */
  public Flux<ChatMessage> findMessages(final String chatRoom, final String siteId) {
    return chatMessageRepository.findAllByChatRoomAndSiteIdAndIsDeleteFalse(chatRoom, siteId);
  }

  /**
   * 채팅 메시지 정보를 리턴.
   *
   * @param id the id
   * @return mono
   */
  public Mono<ChatMessage> findById(String id) {
    return chatMessageRepository.findById(id);
  }

  /**
   * 채팅 메시지 삭제.
   *
   * @param entity the entity
   * @return mono
   */
  public Mono<ChatMessage> delete(ChatMessage entity) {
    entity.setIsDelete(true);
    entity.setDeleteDate(LocalDateTime.now());
    return chatMessageRepository.save(entity);
  }
}