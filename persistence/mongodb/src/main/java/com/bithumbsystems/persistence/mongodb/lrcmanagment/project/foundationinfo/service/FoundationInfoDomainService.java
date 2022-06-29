package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.repository.FoundationInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FoundationInfoDomainService {

    private final FoundationInfoRepository foundationInfoRepository;

    public Flux<FoundationInfo> findByFoundationInfo() {
        return foundationInfoRepository.findAll();
    }

    /**
     * 재단정보 id로 찾기
     * @param id
     * @return SubmittedDocumentResponse Object
     */
    public Mono<FoundationInfo> findById(String id) {
        return foundationInfoRepository.findById(id);
    }

    /**
     * 재단정보 여러개 저장 및 업데이트
     * @param foundationInfo
     * @return FoundationInfoResponse Object
     */
    public Mono<FoundationInfo> updateFoundationInfo(FoundationInfo foundationInfo) {
        return foundationInfoRepository.save(foundationInfo);
    }
}
