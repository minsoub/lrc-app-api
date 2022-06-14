package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.model.IcoInfo;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IcoInfoRepository extends ReactiveMongoRepository<IcoInfo, String> {

    Flux<IcoInfo> findByProjectId(String projectId);
}
