package com.bithumbsystems.persistence.mongodb.chat.service;

import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatFile;
import com.bithumbsystems.persistence.mongodb.chat.repository.ChatFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatFileDomainService {

  private final ChatFileRepository chatFileRepository;

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
}
