package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.exception.IcoInfoException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.mapper.IcoInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.request.IcoInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.response.IcoInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.service.IcoInfoDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class IcoInfoService {

    private final IcoInfoDomainService icoInfoDomainService;

    /**
     * 상장 정보 1개 id 찾기
     *
     * @param projectId
     * @return IcoInfoResponse Object
     */
    public Mono<List<IcoInfoResponse>> findByProjectId(String projectId) {
        return icoInfoDomainService.findByProjectId(projectId)
                .map(IcoInfoMapper.INSTANCE::icoInfoResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new IcoInfoException(ErrorCode.NOT_FOUND_CONTENT)));

    }

    /**
     * 상장 정보 여러개 저장 및 업데이트
     *
     * @param icoInfoRequest
     * @return IcoInfoResponse Object
     */
    public Mono<List<IcoInfoResponse>> create(String projectId, IcoInfoRequest icoInfoRequest) {
        return Mono.just(icoInfoRequest.getIcoInfoList())
                .flatMapMany(icoInfos -> Flux.fromIterable(icoInfos))
                .flatMap(icoInfo ->
                    icoInfoDomainService.save(IcoInfoMapper.INSTANCE.icoInfoRequestToIcoInfo(icoInfo))
                )
                .then(this.findByProjectId(projectId));
    }
}
