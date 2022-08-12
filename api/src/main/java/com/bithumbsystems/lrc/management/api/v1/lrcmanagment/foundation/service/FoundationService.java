package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.util.FormatUtil;
import com.bithumbsystems.lrc.management.api.v1.audit.model.response.AuditLogResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.Mapper.FoundationMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.exception.FoundationException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.request.FoundationRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.FoundationResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.IcoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.LinkResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response.MarketResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.mapper.ProjectInfoMapper;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.service.FoundationDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.service.IcoInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.service.MarketingQuantityDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service.ProjectInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.service.ProjectLinkDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service.LineMngDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.service.StatusCodeDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
     * 재단 모든 정보 및 계약 상태 검색
     * @param contractCode
     * @return FoundationResponse
     */
    public Mono<List<FoundationResponse>> getFoundation(String contractCode) {
        return foundationInfoDomainService.findByCustomSearchAll(contractCode)
                .flatMap(foundationInfo ->
                        Mono.just(FoundationResponse.builder()
                                .projectId(foundationInfo.getId())
                                .projectName(foundationInfo.getName())
                                .symbol(foundationInfo.getSymbol())
                                .contractCode(foundationInfo.getContractCode())
                                .progressCode(foundationInfo.getProcessCode())
                                .createDate(foundationInfo.getCreateDate())
                                .build()
                        )
                )
                .flatMap(res -> {

                    Mono<FoundationResponse> res1 = Mono.just(res);

                    return res1.zipWith(projectInfoDomainService.findByProjectId(res.getProjectId())
                                    .map(ProjectInfoMapper.INSTANCE::projectInfoResponse)
                            )
                            .map(tuple -> {
                                tuple.getT1().setBusinessCode(tuple.getT2().getBusinessCode());
                                tuple.getT1().setNetworkCode(tuple.getT2().getNetworkCode());
                                return tuple.getT1();
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );

                })
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
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );
                })
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);

                    return res1.zipWith(marketingQuantityDomainService.findByProjectId(res.getProjectId())
                                    .map(marketingQuantity ->
                                            MarketResponse.builder()
                                                    .minimumQuantity(marketingQuantity.getMinimumQuantity())
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
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );
                })
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);

                    return res1.zipWith(projectLinkDomainService.findByProjectLinkList(res.getProjectId())
                                    .flatMap(projectLink ->
                                            foundationInfoDomainService.findById(projectLink.getProjectId())
                                                    .map(projectInfo -> LinkResponse.builder()
                                                            .projectId(res.getProjectId())
                                                            .projectName(projectInfo.getName())
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
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getBusinessCode())) {
                        return lineMngDomainService.findById(res.getBusinessCode())
                                .map(business -> {
                                    res.setBusinessName(business.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }

                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getNetworkCode())) {
                        return lineMngDomainService.findById(res.getNetworkCode())
                                .map(business -> {
                                    res.setNetworkName(business.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
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
    }

    /**
     * 프로젝트 검색 시 심볼에 의해서 Like 검색을 수행해서 리턴한다.
     *
     * @param keyWord
     * @return
     */
    public Mono<List<FoundationResponse>> getFoundationKeyWordSearch(String keyWord) {
        return foundationInfoDomainService.findBySymbolSearch(keyWord)
                .flatMap(foundationInfo ->
                        Mono.just(FoundationResponse.builder()
                                .projectId(foundationInfo.getId())
                                .projectName(foundationInfo.getName())
                                .symbol(foundationInfo.getSymbol())
                                .contractCode(foundationInfo.getContractCode())
                                .progressCode(foundationInfo.getProcessCode())
                                .createDate(foundationInfo.getCreateDate())
                                .build()
                        )
                )
                .collectSortedList(Comparator.comparing(FoundationResponse::getCreateDate));
    }

    /**
     * 재단 1개 id 찾기
     *
     * @param projectId
     * @return FoundationInfoResponse Object
     */
    public Mono<FoundationResponse> findByProjectId(String projectId) {
        return foundationDomainService.findByProjectId(projectId)
                .map(FoundationMapper.INSTANCE::foundationResponse);
    }

    /**
     * 재단 1개 저장
     * @param foundationRequest
     * @return FoundationResponse
     */
    public Mono<FoundationResponse> create(FoundationRequest foundationRequest) {
        return foundationDomainService.save(FoundationMapper.INSTANCE.foundationRequestToFoundation(foundationRequest))
                .map(FoundationMapper.INSTANCE::foundationResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_CREATE_CONTENT)));
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
                .switchIfEmpty(Mono.error(new FoundationException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * 재단 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param businessCode 사업계열
     * @param networkCode 네트워크계열
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return Foundation Object
     */
    public Mono<List<FoundationResponse>> getFoundationSearch(LocalDate fromDate, LocalDate toDate, String contractCode, String progressCode,
                                                              List<String> businessCode, List<String> networkCode, String keyword) {

        return foundationInfoDomainService.findByCustomSearch(fromDate, toDate, contractCode, progressCode, keyword)
                .flatMap(foundationInfo ->
                        Mono.just(FoundationResponse.builder()
                                .id(foundationInfo.getId())
                                .projectId(foundationInfo.getId())
                                .projectName(foundationInfo.getName())
                                .symbol(foundationInfo.getSymbol())
                                .contractCode(foundationInfo.getContractCode())
                                .progressCode(foundationInfo.getProcessCode())
                                .createDate(foundationInfo.getCreateDate())
                                .build()
                        )
                )
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);
                    if (businessCode.size() > 0 || networkCode.size() > 0) {  // 검색 조건이 있다면..
                        return res1.zipWith(projectInfoDomainService.findByProjectInfo(res.getProjectId(), businessCode, networkCode)
                                        .map(ProjectInfoMapper.INSTANCE::projectInfoResponse)
                                        .collectList()
                                )
                                .filter(tuple->{
                                    log.debug("tuple get T2 value => {}", tuple.getT2());
                                    return tuple.getT2().size() > 0;
                                })
                                .map(tuple -> {
                                    tuple.getT1().setBusinessCode(
                                            tuple.getT2().stream().map(t -> t.getBusinessCode()).collect(Collectors.joining())
                                    );
                                    tuple.getT1().setNetworkCode(
                                            tuple.getT2().stream().map(t -> t.getNetworkCode()).collect(Collectors.joining())
                                    );
                                    return tuple.getT1();
                                });
//                                .switchIfEmpty(
//                                        Mono.defer(() -> Mono.just(FoundationResponse.builder()
//                                                .id(res.getProjectId())
//                                                .projectId(res.getProjectId())
//                                                .projectName(res.getProjectName())
//                                                .symbol(res.getSymbol())
//                                                .contractCode(res.getContractCode())
//                                                .contractName(res.getContractName())
//                                                .progressCode(res.getProgressCode())
//                                                .progressName(res.getProgressName())
//                                                .businessCode(res.getBusinessCode())
//                                                .businessName(res.getBusinessName())
//                                                .networkCode(res.getNetworkCode())
//                                                .networkName(res.getNetworkName())
//                                                .minimumQuantity(res.getMinimumQuantity())
//                                                .actualQuantity(res.getActualQuantity())
//                                                .projectLink(res.getProjectLink())
//                                                .icoDate(res.getIcoDate())
//                                                .createDate(res.getCreateDate())
//                                                .build()))
//                                );
                    }else { // 검색 조건이 없다면..
                        return res1.zipWith(projectInfoDomainService.findByProjectId(res.getProjectId()) // .findByProjectInfo(res.getProjectId(), businessCode, networkCode)
                                        .map(ProjectInfoMapper.INSTANCE::projectInfoResponse)
                                       // .collectList()
                                )
                                .map(tuple -> {
                                    tuple.getT1().setBusinessCode(
                                            tuple.getT2().getBusinessCode()
                                            //tuple.getT2().map(t -> t.getBusinessCode()).collect(Collectors.joining())
                                    );
                                    tuple.getT1().setNetworkCode(
                                            tuple.getT2().getNetworkCode()
                                            //tuple.getT2().stream().map(t -> t.getNetworkCode()).collect(Collectors.joining())
                                    );
                                    return tuple.getT1();
                                })
                                .switchIfEmpty(
                                        Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                                .id(res.getProjectId())
                                                .projectId(res.getProjectId())
                                                .projectName(res.getProjectName())
                                                .symbol(res.getSymbol())
                                                .contractCode(res.getContractCode())
                                                .contractName(res.getContractName())
                                                .progressCode(res.getProgressCode())
                                                .progressName(res.getProgressName())
                                                .businessCode(res.getBusinessCode())
                                                .businessName(res.getBusinessName())
                                                .networkCode(res.getNetworkCode())
                                                .networkName(res.getNetworkName())
                                                .minimumQuantity(res.getMinimumQuantity())
                                                .actualQuantity(res.getActualQuantity())
                                                .projectLink(res.getProjectLink())
                                                .icoDate(res.getIcoDate())
                                                .createDate(res.getCreateDate())
                                                .build()))
                                );
                    }


                })
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
                                                t.getIcoDate() == null ? "" : t.getIcoDate().toString() + " (" + t.getMarketInfo() + ")"
                                        ).collect(Collectors.joining(", "))
                                );
                                return tuple.getT1();
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .id(res.getProjectId())
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );
                })
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);

                    return res1.zipWith(marketingQuantityDomainService.findByProjectId(res.getProjectId())
                                    .map(marketingQuantity ->
                                            MarketResponse.builder()
                                                    .minimumQuantity(marketingQuantity.getMinimumQuantity())
                                                    .actualQuantity(marketingQuantity.getActualQuantity())
                                                    .symbol(marketingQuantity.getSymbol())
                                                    .build()
                                    )
                                    .collectList()
                            )
                            .map(tuple -> {
                                tuple.getT1().setMinimumQuantity(
                                        tuple.getT2().stream().map(t ->
                                                FormatUtil.formatCurrency(t.getMinimumQuantity()) + " " + t.getSymbol()
                                        ).collect(Collectors.joining(", "))
                                );
                                tuple.getT1().setActualQuantity(
                                        tuple.getT2().stream().map(t ->
                                                FormatUtil.formatCurrency(t.getActualQuantity()) + " " + t.getSymbol()
                                        ).collect(Collectors.joining(", "))
                                );

                                return tuple.getT1();
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .id(res.getProjectId())
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );
                })
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);
                    // 연결 프로젝트
                    return res1.zipWith(projectLinkDomainService.findByProjectLinkList(res.getProjectId())
                                    .flatMap(projectLink ->
                                            foundationInfoDomainService.findById(projectLink.getLinkProjectId())
                                                    .map(projectInfo -> LinkResponse.builder()
                                                            .projectId(res.getProjectId())
                                                            .projectName(projectInfo.getName())
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
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .id(res.getProjectId())
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getBusinessCode())) {
                        return lineMngDomainService.findById(res.getBusinessCode())
                                .map(business -> {
                                    res.setBusinessName(business.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getNetworkCode())) {
                        return lineMngDomainService.findById(res.getNetworkCode())
                                .map(business -> {
                                    res.setNetworkName(business.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getProgressCode())) {
                        return statusCodeDomainService.findStatusValueById(res.getProgressCode())
                                .map(progress -> {
                                    res.setProgressName(progress.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getContractCode())) {
                        return statusCodeDomainService.findStatusValueById(res.getContractCode())
                                .map(progress -> {
                                    res.setContractName(progress.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
                .collectSortedList(Comparator.comparing(FoundationResponse::getCreateDate).reversed());

    }


    /**
     * 재단 검색 Excel download
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param businessCode 사업계열
     * @param networkCode 네트워크계열
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return Foundation Object
     */
    public Mono<ByteArrayInputStream> downloadExcel(LocalDate fromDate, LocalDate toDate, String contractCode, String progressCode,
                                                    List<String> businessCode, List<String> networkCode, String keyword) {

        return foundationInfoDomainService.findByCustomSearch(fromDate, toDate, contractCode, progressCode, keyword)
                .flatMap(foundationInfo ->
                        Mono.just(FoundationResponse.builder()
                                .id(foundationInfo.getId())
                                .projectId(foundationInfo.getId())
                                .projectName(foundationInfo.getName())
                                .symbol(foundationInfo.getSymbol())
                                .contractCode(foundationInfo.getContractCode())
                                .progressCode(foundationInfo.getProcessCode())
                                .createDate(foundationInfo.getCreateDate())
                                .build()
                        )
                )
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);
                    if (businessCode.size() > 0 || networkCode.size() > 0) {  // 검색 조건이 있다면..
                        return res1.zipWith(projectInfoDomainService.findByProjectInfo(res.getProjectId(), businessCode, networkCode)
                                        .map(ProjectInfoMapper.INSTANCE::projectInfoResponse)
                                        .collectList()
                                )
                                .filter(tuple->{
                                    log.debug("tuple get T2 value => {}", tuple.getT2());
                                    return tuple.getT2().size() > 0;
                                })
                                .map(tuple -> {
                                    tuple.getT1().setBusinessCode(
                                            tuple.getT2().stream().map(t -> t.getBusinessCode()).collect(Collectors.joining())
                                    );
                                    tuple.getT1().setNetworkCode(
                                            tuple.getT2().stream().map(t -> t.getNetworkCode()).collect(Collectors.joining())
                                    );
                                    return tuple.getT1();
                                });
                    }else { // 검색 조건이 없다면..
                        return res1.zipWith(projectInfoDomainService.findByProjectId(res.getProjectId()) // .findByProjectInfo(res.getProjectId(), businessCode, networkCode)
                                                .map(ProjectInfoMapper.INSTANCE::projectInfoResponse)
                                        // .collectList()
                                )
                                .map(tuple -> {
                                    tuple.getT1().setBusinessCode(
                                            tuple.getT2().getBusinessCode()
                                            //tuple.getT2().map(t -> t.getBusinessCode()).collect(Collectors.joining())
                                    );
                                    tuple.getT1().setNetworkCode(
                                            tuple.getT2().getNetworkCode()
                                            //tuple.getT2().stream().map(t -> t.getNetworkCode()).collect(Collectors.joining())
                                    );
                                    return tuple.getT1();
                                })
                                .switchIfEmpty(
                                        Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                                .id(res.getProjectId())
                                                .projectId(res.getProjectId())
                                                .projectName(res.getProjectName())
                                                .symbol(res.getSymbol())
                                                .contractCode(res.getContractCode())
                                                .contractName(res.getContractName())
                                                .progressCode(res.getProgressCode())
                                                .progressName(res.getProgressName())
                                                .businessCode(res.getBusinessCode())
                                                .businessName(res.getBusinessName())
                                                .networkCode(res.getNetworkCode())
                                                .networkName(res.getNetworkName())
                                                .minimumQuantity(res.getMinimumQuantity())
                                                .actualQuantity(res.getActualQuantity())
                                                .projectLink(res.getProjectLink())
                                                .icoDate(res.getIcoDate())
                                                .createDate(res.getCreateDate())
                                                .build()))
                                );
                    }


                })
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
                                                t.getIcoDate() == null ? "" : t.getIcoDate().toString() + " (" + t.getMarketInfo() + ")"
                                        ).collect(Collectors.joining(", "))
                                );
                                return tuple.getT1();
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .id(res.getProjectId())
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );
                })
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);

                    return res1.zipWith(marketingQuantityDomainService.findByProjectId(res.getProjectId())
                                    .map(marketingQuantity ->
                                            MarketResponse.builder()
                                                    .minimumQuantity(marketingQuantity.getMinimumQuantity())
                                                    .actualQuantity(marketingQuantity.getActualQuantity())
                                                    .symbol(marketingQuantity.getSymbol())
                                                    .build()
                                    )
                                    .collectList()
                            )
                            .map(tuple -> {
                                tuple.getT1().setMinimumQuantity(
                                        tuple.getT2().stream().map(t ->
                                                FormatUtil.formatCurrency(t.getMinimumQuantity()) + " " + t.getSymbol()
                                        ).collect(Collectors.joining(", "))
                                );
                                tuple.getT1().setActualQuantity(
                                        tuple.getT2().stream().map(t ->
                                                FormatUtil.formatCurrency(t.getActualQuantity()) + " " + t.getSymbol()
                                        ).collect(Collectors.joining(", "))
                                );

                                return tuple.getT1();
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .id(res.getProjectId())
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );
                })
                .flatMap(res -> {
                    Mono<FoundationResponse> res1 = Mono.just(res);
                    // 연결 프로젝트
                    return res1.zipWith(projectLinkDomainService.findByProjectLinkList(res.getProjectId())
                                    .flatMap(projectLink ->
                                            foundationInfoDomainService.findById(projectLink.getLinkProjectId())
                                                    .map(projectInfo -> LinkResponse.builder()
                                                            .projectId(res.getProjectId())
                                                            .projectName(projectInfo.getName())
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
                            })
                            .switchIfEmpty(
                                    Mono.defer(() -> Mono.just(FoundationResponse.builder()
                                            .id(res.getProjectId())
                                            .projectId(res.getProjectId())
                                            .projectName(res.getProjectName())
                                            .symbol(res.getSymbol())
                                            .contractCode(res.getContractCode())
                                            .contractName(res.getContractName())
                                            .progressCode(res.getProgressCode())
                                            .progressName(res.getProgressName())
                                            .businessCode(res.getBusinessCode())
                                            .businessName(res.getBusinessName())
                                            .networkCode(res.getNetworkCode())
                                            .networkName(res.getNetworkName())
                                            .minimumQuantity(res.getMinimumQuantity())
                                            .actualQuantity(res.getActualQuantity())
                                            .projectLink(res.getProjectLink())
                                            .icoDate(res.getIcoDate())
                                            .createDate(res.getCreateDate())
                                            .build()))
                            );
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getBusinessCode())) {
                        return lineMngDomainService.findById(res.getBusinessCode())
                                .map(business -> {
                                    res.setBusinessName(business.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getNetworkCode())) {
                        return lineMngDomainService.findById(res.getNetworkCode())
                                .map(business -> {
                                    res.setNetworkName(business.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getProgressCode())) {
                        return statusCodeDomainService.findStatusValueById(res.getProgressCode())
                                .map(progress -> {
                                    res.setProgressName(progress.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
                .flatMap(res -> {
                    if(!StringUtils.isEmpty(res.getContractCode())) {
                        return statusCodeDomainService.findStatusValueById(res.getContractCode())
                                .map(progress -> {
                                    res.setContractName(progress.getName());
                                    return res;
                                });
                    } else {
                        return Mono.just(res);
                    }
                })
                .collectSortedList(Comparator.comparing(FoundationResponse::getCreateDate).reversed())
                .flatMap(this::createExcelFile);

    }

    private Mono<ByteArrayInputStream> createExcelFile(List<FoundationResponse> fraudReportList) {
        return Mono.fromCallable(() -> {
                    log.debug("엑셀 파일 생성 시작");

                    SXSSFWorkbook workbook = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE);  // keep 100 rows in memory, exceeding rows will be flushed to disk
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    CreationHelper creationHelper = workbook.getCreationHelper();

                    Sheet sheet = workbook.createSheet("거래지원");

                    Font headerFont = workbook.createFont();
                    headerFont.setFontName("맑은 고딕");
                    headerFont.setFontHeight((short) (10 * 20));
                    headerFont.setBold(true);
                    headerFont.setColor(IndexedColors.BLACK.index);

                    Font bodyFont = workbook.createFont();
                    bodyFont.setFontName("맑은 고딕");
                    bodyFont.setFontHeight((short) (10 * 20));

                    // Cell 스타일 생성
                    CellStyle headerStyle = workbook.createCellStyle();
                    headerStyle.setAlignment(HorizontalAlignment.CENTER);
                    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    headerStyle.setFont(headerFont);

                    // Row for Header
                    Row headerRow = sheet.createRow(0);

                    // Header
                    String[] fields = {"프로젝트명", "프로젝트 심볼", "계약상태", "진행상태", "사업계열", "네트워크 계열", "제안 수량(마케팅 수량)", "입금 수량(마케팅 수량)", "상장일", "등록일시"};
                    for (int col = 0; col < fields.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        cell.setCellValue(fields[col]);
                        cell.setCellStyle(headerStyle);
                    }

                    // Body
                    int rowIdx = 1;
                    for (FoundationResponse res : fraudReportList) {
                        Row row = sheet.createRow(rowIdx++);

                        row.createCell(0).setCellValue(res.getProjectName());
                        row.createCell(1).setCellValue(res.getSymbol());
                        row.createCell(2).setCellValue(res.getContractName());
                        row.createCell(3).setCellValue(res.getProgressName());
                        row.createCell(4).setCellValue(res.getBusinessName());
                        row.createCell(5).setCellValue(res.getNetworkName());
                        row.createCell(6).setCellValue(res.getMinimumQuantity());
                        row.createCell(7).setCellValue(res.getActualQuantity());
                        row.createCell(8).setCellValue(res.getIcoDate());
                        row.createCell(9).setCellValue(res.getCreateDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    }
                    workbook.write(out);

                    log.debug("엑셀 파일 생성 종료");
                    return new ByteArrayInputStream(out.toByteArray());
                })
                .log();
    }
}
