package com.bithumbsystems.persistence.mongodb.chat.service;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatFile;
import com.bithumbsystems.persistence.mongodb.chat.repository.ChatFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Chat file domain service.
 */
@Service
@RequiredArgsConstructor
public class ChatFileDomainService {

  private final ChatFileRepository chatFileRepository;

  /**
   * 채팅 파일 리스트 조회 by projectId.
   *
   * @param projectId the project id
   * @return flux
   */
  public Flux<ChatFile> findByList(String projectId) {
    return chatFileRepository.findByProjectId(projectId);
  }

  /**
   * 파일 상세 정보 조회.
   *
   * @param fileKey the file key
   * @return mono
   */
  public Mono<ChatFile> findByFileId(String fileKey) {
    return chatFileRepository.findById(fileKey);
  }

  /**
   * 채팅 파일을 저장한다.
   *
   * @param chatFile the chat file
   * @return mono
   */
  public Mono<ChatFile> save(ChatFile chatFile) {
    return chatFileRepository.save(chatFile);
  }
}
