package com.bithumbsystems.persistence.mongodb.chat.service;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import com.bithumbsystems.persistence.mongodb.chat.repository.ChatChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * The type Chat channel domain service.
 */
@Service
@RequiredArgsConstructor
public class ChatChannelDomainService {
  private final ChatChannelRepository chatChannelRepository;

  /**
   * 채팅방 정보를 업데이트 한다.- 프로젝트 추가
   *
   * @param chatChannel the chat channel
   * @return mono
   */
  public Mono<ChatChannel> update(ChatChannel chatChannel) {
    return chatChannelRepository.save(chatChannel);
  }

  /**
   * 채팅방에 등록된 사용자 정보를 리턴한다.
   *
   * @param accountId the account id
   * @return mono
   */
  public Mono<ChatChannel> findByAccountId(String accountId) {
    return chatChannelRepository.findByAccountId(accountId);
  }

  /**
   * 채팅방에 신규 사용자를 등록한다.
   *
   * @param chatChannel the chat channel
   * @return mono
   */
  public Mono<ChatChannel> insert(ChatChannel chatChannel) {
    return chatChannelRepository.insert(chatChannel);
  }

  /**
   * Find by account id and role and chat rooms contains mono.
   *
   * @param accountId the account id
   * @param role      the role
   * @param chatRoom  the chat room
   * @return the mono
   */
  public Mono<ChatChannel> findByAccountIdAndRoleAndChatRoomsContains(String accountId, ChatRole role, String chatRoom) {
    return chatChannelRepository.findByAccountIdAndRoleAndChatRoomsContains(accountId, role, chatRoom);
  }
}
