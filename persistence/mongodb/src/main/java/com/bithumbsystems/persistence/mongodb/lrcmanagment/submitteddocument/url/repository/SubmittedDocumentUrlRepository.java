package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.model.entity.SubmittedDocumentUrl;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SubmittedDocumentUrlRepository extends ReactiveMongoRepository<SubmittedDocumentUrl, String> {

    /**
     * 제출 서류 관리 id, type 으로 url 찾기
     * @param projectId
     * @param type
     * @return SubmittedDocumentUrlResponse Object
     */
    Flux<SubmittedDocumentUrl> findByProjectIdAndType(String projectId, SubmittedDocumentEnums type);

    /**
     * 제출 서류 관리 : 프로젝트 아이디로 찾기
     * @param projectId
     * @return
     */
    Flux<SubmittedDocumentUrl> findByProjectId(String projectId);
}
