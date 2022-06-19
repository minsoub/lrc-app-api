package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.mapper.FoundationInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.request.FoundationInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.response.FoundationInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FoundationInfoService {

    private final FoundationInfoDomainService foundationInfoDomainService;

    /**
     * 재단 정보 1개 id 찾기
     *
     * @param projectId
     * @return FoundationInfoResponse Object
     */
    public Mono<FoundationInfoResponse> findByProjectId(String projectId) {
        return foundationInfoDomainService.findByProjectId(projectId)
                .map(FoundationInfoMapper.INSTANCE::foundationInfoResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));

    }

    /**
     * 프로젝트 정보 업데이트
     *
     * @param foundationInfoRequest
     * @return FoundationInfoResponse Object
     */
    public Mono<FoundationInfoResponse> updateFoundationInfo(String projectId, FoundationInfoRequest foundationInfoRequest) {
        return foundationInfoDomainService.findByProjectId(projectId)
                .flatMap(c -> {
                    c.setProjectId(foundationInfoRequest.getProjectId());
                    c.setProjectName(foundationInfoRequest.getProjectName());
                    c.setSymbol(foundationInfoRequest.getSymbol());
                    c.setContrectCode(foundationInfoRequest.getContrectCode());
                    c.setContrectName(foundationInfoRequest.getContrectName());
                    c.setProgressCode(foundationInfoRequest.getProgressCode());
                    c.setProgressName(foundationInfoRequest.getProgressName());
                    c.setAdminMemo(foundationInfoRequest.getAdminMemo());

                    return foundationInfoDomainService.updateFoundationInfo(c)
                            .map(FoundationInfoMapper.INSTANCE::foundationInfoResponse);
                })
                .switchIfEmpty(
                        foundationInfoDomainService.updateFoundationInfo(FoundationInfoMapper.INSTANCE.foundationInfoRequestToFoundationInfo(foundationInfoRequest))
                                .map(FoundationInfoMapper.INSTANCE::foundationInfoResponse)
                        //Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT))
                );
    }
}
