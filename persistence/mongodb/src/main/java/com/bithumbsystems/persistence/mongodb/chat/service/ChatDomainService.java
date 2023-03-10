package com.bithumbsystems.persistence.mongodb.chat.service;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatFile;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.chat.repository.ChatFileRepository;
import com.bithumbsystems.persistence.mongodb.chat.repository.ChatMessageRepository;
import com.bithumbsystems.persistence.mongodb.chat.repository.ChatRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatDomainService {
    private final ChatRepository chatRepository;
    private final ChatFileRepository chatFileRepository;

    private final ChatMessageRepository chatMessageRepository;

    /**
     * 채팅방에 등록된 사용자 정보를 리턴한다.
     *
     * @param accountId
     * @return
     */
    public Mono<ChatChannel> findByAccountId(String accountId) {
        return chatRepository.findByAccountId(accountId);
    }

    /**
     * 채팅방에 신규 사용자를 등록한다.
     *
     * @param chatChannel
     * @return
     */
    public Mono<ChatChannel> insert(ChatChannel chatChannel) {
        return chatRepository.insert(chatChannel);
    }

    /**
     * 채팅방 정보를 업데이트 한다.- 프로젝트 추가
     * @param chatChannel
     * @return
     */
    public Mono<ChatChannel> update(ChatChannel chatChannel) {
        return chatRepository.save(chatChannel);
    }

    /**
     * 채팅 파일 리스트 조회 by projectId
     * @param projectId
     * @return
     */
    public Flux<ChatFile> findByList(String projectId) {
        return chatFileRepository.findByProjectId(projectId);
    }

    /**
     * 파일 상세 정보 조회
     * @param fileKey
     * @return
     */
    public Mono<ChatFile> findByFileId(String fileKey) {
        return chatFileRepository.findById(fileKey);
    }

    /**
     * 채팅 파일을 저장한다.
     *
     * @param chatFile
     * @return
     */
    public Mono<ChatFile> save(ChatFile chatFile) {
        return chatFileRepository.save(chatFile);
    }

    public Mono<ChatFile> findByChatFileId(String id) {
        return chatFileRepository.findById(id);
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

    /**
     * chat 메시지 리스트를 가져온다.
     *
     * @param roomId
     * @param siteId
     * @return
     */
    public Flux<ChatMessage> findChatMessage(String roomId, String siteId) {
        return chatMessageRepository.findByChatRoomAndSiteId(roomId, siteId);
    }
}
