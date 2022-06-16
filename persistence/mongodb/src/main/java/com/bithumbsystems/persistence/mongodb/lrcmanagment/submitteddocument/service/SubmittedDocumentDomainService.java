package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.SubmittedDocument;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.repository.SubmittedDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SubmittedDocumentDomainService {

    private final SubmittedDocumentRepository submittedDocumentRepository;


    /**
     * 제출 서류 관리 id로 찾기
     * @param projectId
     * @return SubmittedDocumentResponse Object
     */
    public Flux<SubmittedDocument> findByProjectId(String projectId) {
        return submittedDocumentRepository.findByProjectId(projectId);
    }

    /**
     * 제출 서류 관리 여러개 저장 및 업데이트
     * @param submittedDocument
     * @return SubmittedDocumentResponse Object
     */
    public Mono<SubmittedDocument> save(SubmittedDocument submittedDocument) {
        return submittedDocumentRepository.save(submittedDocument);
    }
}
