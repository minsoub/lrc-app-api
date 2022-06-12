package com.bithumbsystems.persistence.mongodb.servicelog.service;

import com.bithumbsystems.persistence.mongodb.servicelog.model.ServiceLog;
import com.bithumbsystems.persistence.mongodb.servicelog.repository.ServiceLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ServiceLogDomainService {

    private final ServiceLogRepository serviceLogRepository;

    public Flux<ServiceLog> findAll() {
        return serviceLogRepository.findAll();
    }

    public Mono<ServiceLog> save(ServiceLog serviceLog) {
        return serviceLogRepository.insert(serviceLog);
    }
}
