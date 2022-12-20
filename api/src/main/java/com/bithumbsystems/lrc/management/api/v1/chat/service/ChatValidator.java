package com.bithumbsystems.lrc.management.api.v1.chat.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.exception.LrcException;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import com.bithumbsystems.persistence.mongodb.chat.service.ChatChannelDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatValidator {

  private final ChatChannelDomainService chatChannelDomainService;

  public Mono<ChatChannel> checkValidChatRoom(final Account account, final String chatRoom) {
    return chatChannelDomainService.findByAccountIdAndRoleAndChatRoomsContains(
            account.getAccountId(), ChatRole.ADMIN, chatRoom)
        .switchIfEmpty(Mono.error(new LrcException(ErrorCode.INVALID_CHAT_ROOM)));
  }
}