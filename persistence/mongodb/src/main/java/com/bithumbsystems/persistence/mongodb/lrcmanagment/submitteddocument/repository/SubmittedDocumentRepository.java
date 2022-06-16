package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.SubmittedDocument;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SubmittedDocumentRepository extends ReactiveMongoRepository<SubmittedDocument, String> {

    Flux<SubmittedDocument> findByProjectId(String projectId);
}
