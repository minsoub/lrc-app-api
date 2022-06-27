package com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.service;

import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity.StatusCode;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.repository.StatusCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class StatusCodeDomainService {

    private final StatusCodeRepository statusValueRepository;

    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueList
     */
    public Flux<StatusCode> findAllStatus() {
        return statusValueRepository.findAll();
    }

    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueList
     */
    public Flux<StatusCode> findAllTree() {
        return statusValueRepository.findAll();
    }

    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueList
     */
    public Flux<StatusCode> findParentCode(String parentCode) {
        return statusValueRepository.findByParentCode(parentCode);
    }

    /**
     * 상태값 관리 1개 찾기
     * @param id
     * @return
     */
    public Mono<StatusCode> findStatusValueById(String id) {
        return statusValueRepository.findById(id);
    }

    /**
     * 상태값 관리 1개 저장
     * @param lrcStatusCode
     * @return StatusValueList
     */
    public Mono<StatusCode> save(StatusCode lrcStatusCode) {
        return statusValueRepository.insert(lrcStatusCode);
    }

    /**
     * 상태값 관리 수정.
     * @param lrcStatusCode
     * @return
     */
    public Mono<StatusCode> update(StatusCode lrcStatusCode) {
        return statusValueRepository.save(lrcStatusCode);
    }


    /**
     * 상태값 관리 1개 삭제
     * @return N/A
     */
    public Mono<Void> deleteStatusValue(StatusCode lrcStatusCode) {
        return statusValueRepository.delete(lrcStatusCode);
    }


}
