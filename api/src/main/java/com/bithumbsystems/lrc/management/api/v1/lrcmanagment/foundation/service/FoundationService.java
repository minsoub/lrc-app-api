package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.Mapper.FoundationMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.request.FoundationRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.FoundationResponse;
료import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.IcoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.LinkResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.MarketResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.service.FoundationDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.service.IcoInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.service.MarketingQuantityDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service.ProjectInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.service.ProjectLinkDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service.LineMngDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.service.StatusCodeDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FoundationService {

    private final FoundationDomainService foundationDomainService;


    private final FoundationInfoDomainService foundationInfoDomainService;
    private final ProjectInfoDomainService projectInfoDomainService;
    private final IcoInfoDomainService icoInfoDomainService;
    private final MarketingQuantityDomainService marketingQuantityDomainService;
    private final ProjectLinkDomainService projectLinkDomainService;
    private final LineMngDomainService lineMngDomainService;
    private final StatusCodeDomainService statusCodeDomainService;

    /**
     * 재단 모든 정보
     * @return FoundationResponse
     */
    public Mono<List<FoundationResponse>> getFoundation1() {
        return foundationInfoDomainService.findByFoundationInfo()
                .flatMap(foundationInfo ->
                        Mono.just(FoundationResponse.builder()
                        .projectId(foundationInfo.getId())
                        .projectName(foundationInfo.getProjectName())
                        .symbol(foundationInfo.getSymbol())
                        .contractCode(foundationInfo.getContractCode())
                        .progressCode(foundationInfo.getProgressCode())
                        .createDate(foundationInfo.getCreateDate())
                        .build()
                        )
                )
                .flatMap(res -> projectInfoDomainService.findByProjectId(res.getProjectId())
                        .map(projectInfo -> {
                            res.setBusinessCode(projectInfo.getBusinessCode());
                            res.setNetworkCode(projectInfo.getNetworkCode());
                            return res;
                        })
                )
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);

                    return res1.zipWith(icoInfoDomainService.findByProjectId(res.getProjectId())
                                    .map(icoInfo -> IcoResponse.builder()
                                            .marketInfo(icoInfo.getMarketInfo())
                                            .icoDate(icoInfo.getIcoDate())
                                            .build()
                                    ).collectList()
                            )
                            .map(tuple -> {
                                tuple.getT1().setIcoDate(
                                        tuple.getT2().stream().map(t ->
                                                t.getIcoDate().toString() + " (" + t.getMarketInfo() + ")"
                                        ).collect(Collectors.joining(", "))
                                );
                                return tuple.getT1();
                            });
                })
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);

                    return res1.zipWith(marketingQuantityDomainService.findByProjectId(res.getProjectId())
                                    .map(marketingQuantity ->
                                            MarketResponse.builder()
                                                    .MinimumQuantity(marketingQuantity.getMinimumQuantity())
                                                    .actualQuantity(marketingQuantity.getActualQuantity())
                                                    .symbol(marketingQuantity.getSymbol())
                                                    .build()
                                    )
                                    .collectList()
                            )
                            .map(tuple -> {
                                tuple.getT1().setMinimumQuantity(
                                        tuple.getT2().stream().map(t ->
                                                t.getMinimumQuantity() + " " + t.getSymbol()
                                        ).collect(Collectors.joining(", "))
                                );
                                tuple.getT1().setActualQuantity(
                                        tuple.getT2().stream().map(t ->
                                                t.getActualQuantity() + " " + t.getSymbol()
                                        ).collect(Collectors.joining(", "))
                                );

                                return tuple.getT1();
                            });
                })
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);

                    return res1.zipWith(projectLinkDomainService.findByProjectLinkList(res.getProjectId())
                                    .flatMap(projectLink ->
                                            foundationInfoDomainService.findById(projectLink.getProjectId())
                                                    .map(projectInfo -> LinkResponse.builder()
                                                            .projectId(res.getProjectId())
                                                            .projectName(projectInfo.getProjectName())
                                                            .symbol(projectLink.getSymbol())
                                                            .build()
                                                    )
                                    )
                                    .collectList()
                            )
                            .map(tuple -> {
                                tuple.getT1().setProjectLink(
                                        tuple.getT2().stream().map(t ->
                                                t.getProjectName() + " (" + t.getSymbol() +")"
                                        ).collect(Collectors.joining(", "))
                                );
                                return tuple.getT1();
                            });
                })
                .flatMap(res -> lineMngDomainService.findById(res.getBusinessCode())
                        .map(business -> {
                            res.setBusinessName(business.getName());
                            return res;
                        })
                )
                .flatMap(res -> lineMngDomainService.findById(res.getNetworkCode())
                        .map(business -> {
                            res.setNetworkName(business.getName());
                            return res;
                        })
                )
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getProgressCode())) {
                        return statusCodeDomainService.findStatusValueById(res.getProgressCode())
                                .map(progress -> {
                                    res.setProgressName(progress.getName());
                                    return res;
                                });
                    }
                    else {
                        return Mono.just(res);
                    }
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getProgressCode())) {
                        return statusCodeDomainService.findStatusValueById(res.getContractCode())
                                .map(progress -> {
                                    res.setContractName(progress.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
                .collectSortedList(Comparator.comparing(FoundationResponse::getCreateDate));
//                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }


    /**
     * 재단 모든 정보
     * @return FoundationResponse
     */
    public Mono<List<FoundationResponse>> getFoundation() {
        return foundationDomainService.findAll().map(FoundationMapper.INSTANCE::foundationResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 재단 1개 id 찾기
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
     * 재단 1개 저장
     * @param foundationRequest
     * @return FoundationResponse
     */
    public Mono<FoundationResponse> create(FoundationRequest foundationRequest) {
        return foundationDomainService.save(FoundationMapper.INSTANCE.foundationRequestToFoundation(foundationRequest))
                .map(FoundationMapper.INSTANCE::foundationResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 재단 1개 업데이트
     * @param foundationRequest
     * @return FoundationResponse
     */
    public Mono<FoundationResponse> updateFoundationInfo(String id, FoundationRequest foundationRequest) {
        return foundationDomainService.findByProjectId(id)
                .flatMap(c -> {
                    c.setProjectId(foundationRequest.getProjectId());
                    c.setProjectName(foundationRequest.getProjectName());
                    c.setSymbol(foundationRequest.getSymbol());
                    c.setContractCode(foundationRequest.getContractCode());
                    c.setContractName(foundationRequest.getContractName());
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

    /**
     * 재단 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param business 사업계열
     * @param network 네트워크계열
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return Foundation Object
     */
    public Mono<List<FoundationResponse>> findSearch(LocalDateTime fromDate, LocalDateTime toDate, String contractCode, String progressCode,
                                                     List<String> business, List<String> network, String keyword) {
        return foundationDomainService.findSearch(fromDate, toDate, contractCode, progressCode, business, network, keyword)
                .map(FoundationMapper.INSTANCE::foundationResponse)
                .collectList();
    }
}
