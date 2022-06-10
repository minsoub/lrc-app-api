package com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.service;

import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity.StatusValueList;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.repository.StatusValueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StatusValueDomainService {

    private final StatusValueRepository statusValueRepository;

    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueList
     */
    public Flux<StatusValueList> findAllStatus() {
        return statusValueRepository.findAll();
    }

    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueList
     */
    public Flux<StatusValueList> findAllTree() {
        return statusValueRepository.findAll();
    }

    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueList
     */
    public Flux<StatusValueList> findParentCode(String parentCode) {
        return statusValueRepository.findByParentCode(parentCode);
    }

    /**
     * 상태값 관리 1개 저장
     * @param statusValueList
     * @return StatusValueList
     */
    public Mono<StatusValueList> save(StatusValueList statusValueList) {
        return statusValueRepository.insert(statusValueList);
    }
}
