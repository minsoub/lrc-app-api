package com.bithumbsystems.lrc.management.api.v1.dashboard.service;


import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardLineMngResponse;
import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardStatusCodeResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service.ProjectInfoDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service.LineMngDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.service.StatusCodeDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final StatusCodeDomainService statusCodeDomainService;
    private final LineMngDomainService lineMngDomainService;
    private final FoundationInfoDomainService foundationInfoDomainService;
    private final ProjectInfoDomainService projectInfoDomainService;

    /**
     * 상태값 관리 모두 가져오기
     * @return DashBoardStatusCodeResponse
     */
    public Mono<List<DashBoardStatusCodeResponse>> getStatusCode() {
        return statusCodeDomainService.findAllStatus()
                .map(statusCode ->
                    DashBoardStatusCodeResponse.builder()
                            .id(statusCode.getId())
                            .name(statusCode.getName())
                            .parentCode(statusCode.getParentCode())
                            .orderNo(statusCode.getOrderNo())
                            .useYn(statusCode.getUseYn())
                            .build()
                )
                .filter( f -> f.getUseYn())
                .flatMap(c ->
                        Mono.just(c)
                                .zipWith(foundationInfoDomainService.findByCustomContractProcess(c.getId()).count())
                                .map(t -> {
                                    t.getT1().setCount(t.getT2());
                                    return t.getT1();
                                })
                )
                .collectSortedList(Comparator.comparing(DashBoardStatusCodeResponse::getOrderNo));
    }

    /**
     * 계열관리 통계 모두 가져오기
     * @return DashBoardLineMngResponse
     */
    public Mono<List<DashBoardLineMngResponse>> getLineMng() {
        return lineMngDomainService.findAll()
                .map(lineMng ->
                        DashBoardLineMngResponse.builder()
                                .id(lineMng.getId())
                                .name(lineMng.getName())
                                .type(lineMng.getType())
                                .useYn(lineMng.isUseYn())
                                .build()
                )
                .filter( f -> f.getUseYn())
                .flatMap(c ->
                        Mono.just(c)
                                .zipWith(projectInfoDomainService.findByCustomBusinessNetwork(c.getId()).count())
                                .map(t -> {
                                    t.getT1().setCount(t.getT2());
                                    return t.getT1();
                                })
                )
                .collectList();
    }
}
