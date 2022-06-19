package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.Mapper.FoundationMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.request.FoundationRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.FoundationResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.service.FoundationDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FoundationService {

    private final FoundationDomainService foundationDomainService;

    /**
     * 재단정보 모든 정보
     * @return FoundationResponse
     */
    public Mono<List<FoundationResponse>> getFoundation() {
        return foundationDomainService.findAll().map(FoundationMapper.INSTANCE::foundationResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 재단 정보 1개 id 찾기
     *
     * @param projectId
     * @return FoundationInfoResponse Object
     */
    public Mono<FoundationResponse> findByProjectId(String projectId) {
        return foundationDomainService.findByProjectId(projectId)
                .map(FoundationMapper.INSTANCE::foundationResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));

    }

    /**
     * 재단정보 1개 저장
     * @param foundationRequest
     * @return FoundationResponse
     */
    public Mono<FoundationResponse> create(FoundationRequest foundationRequest) {
        return foundationDomainService.save(FoundationMapper.INSTANCE.foundationRequestToFoundation(foundationRequest))
                .map(FoundationMapper.INSTANCE::foundationResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }


    public Mono<FoundationResponse> updateFoundationInfo(String id, FoundationRequest foundationRequest) {
        return foundationDomainService.findByProjectId(id)
                .flatMap(c -> {
                    c.setProjectId(foundationRequest.getProjectId());
                    c.setProjectName(foundationRequest.getProjectName());
                    c.setSymbol(foundationRequest.getSymbol());
                    c.setContrectCode(foundationRequest.getContrectCode());
                    c.setContrectName(foundationRequest.getContrectName());
                    c.setProgressCode(foundationRequest.getProgressCode());
                    c.setProgressName(foundationRequest.getProgressName());
                    c.setBusinessList(foundationRequest.getBusinessList());
                    c.setNetworkList(foundationRequest.getNetworkList());
                    c.setMarketingMin(foundationRequest.getMarketingMin());
                    c.setMarketingCurrent(foundationRequest.getMarketingCurrent());
                    c.setProjectLink(foundationRequest.getProjectLink());

                    return foundationDomainService.save(c)
                            .map(FoundationMapper.INSTANCE::foundationResponse);
                })
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

}
