package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.entity.SubmittedDocumentFile;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.repository.SubmittedDocumentFileRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Submitted document file domain service.
 */
@Service
@RequiredArgsConstructor
public class SubmittedDocumentFileDomainService {

  private final SubmittedDocumentFileRepository submittedDocumentFileRepository;

  /**
   * 제출 서류 관리 file id 찾기.
   *
   * @param contentId the content id
   * @return mono
   */
  public Mono<SubmittedDocumentFile> findSubmittedDocumentFileById(String contentId) {
    return submittedDocumentFileRepository.findById(contentId);
  }

  /**
   * 제출 서류 관리 project id 으로 file 찾기.
   *
   * @param projectId the project id
   * @return SubmittedDocumentFileResponse Object
   */
  public Flux<SubmittedDocumentFile> findByProjectId(String projectId) {
    return submittedDocumentFileRepository.findByProjectId(projectId);
  }

  /**
   * 제출 서류 관리 id, type 으로 file 찾기.
   *
   * @param projectId the project id
   * @param type      the type
   * @return SubmittedDocumentFileResponse Object
   */
  public Flux<SubmittedDocumentFile> findByProjectIdAndType(String projectId, SubmittedDocumentEnums type) {
    return submittedDocumentFileRepository.findByProjectIdAndType(projectId, type);
  }

  /**
   * 제출 서류 관리 file 저장.
   *
   * @param submittedDocumentFile the submitted document file
   * @return SubmittedDocumentFileResponse Object
   */
  public Mono<SubmittedDocumentFile> save(SubmittedDocumentFile submittedDocumentFile) {
    return submittedDocumentFileRepository.insert(submittedDocumentFile);
  }

  /**
   * 제출 서류 관리 file 삭제.
   *
   * @param submittedDocumentFile the submitted document file
   * @return N /A
   */
  public Mono<Void> deleteSubmittedDocumentFile(SubmittedDocumentFile submittedDocumentFile) {
    return submittedDocumentFileRepository.delete(submittedDocumentFile);
  }
}
