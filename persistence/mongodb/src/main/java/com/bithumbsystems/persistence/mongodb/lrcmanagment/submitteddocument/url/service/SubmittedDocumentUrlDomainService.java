package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.model.entity.SubmittedDocumentUrl;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.repository.SubmittedDocumentUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SubmittedDocumentUrlDomainService {

    private final SubmittedDocumentUrlRepository submittedDocumentUrlRepository;

    /**
     * 제출 서류 관리 url id 찾기
     * @param contentId
     * @return
     */
    public Mono<SubmittedDocumentUrl> findSubmittedDocumentUrlById(String contentId) {
        return submittedDocumentUrlRepository.findById(contentId);
    }
    /**
     * 제출 서류 관리 id, type 으로 url 찾기
     * @param projectId
     * @return SubmittedDocumentUrlResponse Object
     */
    public Flux<SubmittedDocumentUrl> findByProjectId(String projectId) {
        return submittedDocumentUrlRepository.findByProjectId(projectId);
    }
    /**
     * 제출 서류 관리 id, type 으로 url 찾기
     * @param projectId
     * @param type
     * @return SubmittedDocumentUrlResponse Object
     */
    public Flux<SubmittedDocumentUrl> findByProjectIdAndType(String projectId, SubmittedDocumentEnums type) {
        return submittedDocumentUrlRepository.findByProjectIdAndType(projectId, type);
    }

    /**
     * 제출 서류 관리 url 저장
     * @param submittedDocumentUrl
     * @return SubmittedDocumentUrlResponse Object
     */
    public Mono<SubmittedDocumentUrl> save(SubmittedDocumentUrl submittedDocumentUrl) {
        return submittedDocumentUrlRepository.insert(submittedDocumentUrl);
    }

    /**
     * 제출 서류 관리 url 삭제
     * @param submittedDocumentUrl
     * @return N/A
     */
    public Mono<Void> deleteSubmittedDocumentUrl(SubmittedDocumentUrl submittedDocumentUrl) {
        return submittedDocumentUrlRepository.delete(submittedDocumentUrl);
    }
}
