package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.entity.SubmittedDocumentFile;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SubmittedDocumentFileRepository extends ReactiveMongoRepository<SubmittedDocumentFile, String> {

    /**
     * 제출 서류 관리 id, type 으로 file 찾기
     * @param projectId
     * @param type
     * @return SubmittedDocumentFileResponse Object
     */
    Flux<SubmittedDocumentFile> findByProjectIdAndType(String projectId, SubmittedDocumentEnums type);

    /**
     * 제출 서류 관리 - 프로젝트 아이디로 조회
     *
     * @param projectId
     * @return
     */
    Flux<SubmittedDocumentFile> findByProjectId(String projectId);
}
