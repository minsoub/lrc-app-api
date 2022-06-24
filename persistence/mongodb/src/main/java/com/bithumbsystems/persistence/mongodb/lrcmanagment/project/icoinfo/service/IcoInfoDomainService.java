package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.model.entity.IcoInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.repository.IcoInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IcoInfoDomainService {

    private final IcoInfoRepository icoInfoRepository;

    /**
     * 상장 정보 1개 id 찾기
     * @param projectId
     * @return IcoInfoResponse Object
     */
    public Flux<IcoInfo> findByProjectId(String projectId) {
        return icoInfoRepository.findByProjectId(projectId);
    }

    /**
     * 상장 정보 여러개 저장 및 업데이트
     * @param icoInfo
     * @return IcoInfoResponse Object
     */
    public Mono<IcoInfo> save(IcoInfo icoInfo) {
        return icoInfoRepository.save(icoInfo);
    }
}
