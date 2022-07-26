package com.bithumbsystems.lrc.management.api.v1.dashboard.service;


import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardLineMngResponse;
import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardStatus;
import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardStatusCodeResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service.ProjectInfoDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service.LineMngDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.service.StatusCodeDomainService;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

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
                        DashBoardStatus.builder()
                            .id(statusCode.getId())
                            .name(statusCode.getName())
                            .parentCode(statusCode.getParentCode())
                            .orderNo(statusCode.getOrderNo())
                            .useYn(statusCode.getUseYn())
                            .build()
                )
                .filter(DashBoardStatus::getUseYn)
                .flatMap(c ->
                        Mono.just(c)
                                .zipWith(foundationInfoDomainService.findByCustomContractProcess(c.getId()).count())
                                .map(t -> {
                                    t.getT1().setCount(t.getT2());
                                    return t.getT1();
                                })
                )
                .collectSortedList(Comparator.comparing(DashBoardStatus::getOrderNo))
                .map(c -> {

                    List<DashBoardStatus> d = c.stream().filter(f -> f.getParentCode() == null || "".equals(f.getParentCode())).collect(Collectors.toList());

                    return d.stream().map(s ->
                            DashBoardStatusCodeResponse.builder()
                                    .id(s.getId())
                                    .name(s.getName())
                                    .parentCode(s.getParentCode())
                                    .orderNo(s.getOrderNo())
                                    .useYn(s.getUseYn())
                                    .count(s.getCount())
                                    .children(
                                            c.stream().filter(f -> f.getParentCode().equals(s.getId()))
                                                    .sorted(Comparator.comparing(DashBoardStatus::getOrderNo))
                                                    .collect(Collectors.toList())
                                    )
                                    .build()
                    ).collect(Collectors.toList());
                });
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
                                .useYn(lineMng.getUseYn())
                                .build()
                )
                .filter(DashBoardLineMngResponse::getUseYn)
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
