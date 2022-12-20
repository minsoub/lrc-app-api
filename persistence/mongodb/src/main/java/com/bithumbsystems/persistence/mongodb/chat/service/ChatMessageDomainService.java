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

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatMessageDomainService {

    private final ChatMessageRepository chatMessageRepository;

    public Mono<ChatMessage> save(ChatMessage chatMessage) {
        chatMessage.setCreateDate(LocalDateTime.now());
        chatMessage.setId(UUID.randomUUID().toString());
        return chatMessageRepository.save(chatMessage);
    }

    public Flux<ChatMessage> findMessages(final String chatRoom, final String siteId) {
        return chatMessageRepository.findAllByChatRoomAndSiteIdAndIsDeleteFalse(chatRoom, siteId);
    }
    /**
     * 채팅 메시지 정보를 리턴한다.
     *
     * @param id
     * @return
     */
    public Mono<ChatMessage> findById(String id) {
        return chatMessageRepository.findById(id);
    }

    /**
     * 채팅 메시지를 삭제한다.
     *
     * @param entity
     * @return
     */
    public Mono<ChatMessage> delete(ChatMessage entity) {
        entity.setIsDelete(true);
        entity.setDeleteDate(LocalDateTime.now());
        return chatMessageRepository.save(entity);
    }

}