package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.model.entity.ProjectLink;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProjectLinkRepository extends ReactiveMongoRepository<ProjectLink, String> {

    Flux<ProjectLink> findByProjectId(String projectId);
}
