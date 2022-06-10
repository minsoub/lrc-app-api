package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.mapper.StatusValueListMapper;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request.StatusValueListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.response.StatusValueListResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.service.StatusValueDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class StatusValueListService {

    private final StatusValueDomainService statusValueDomainService;

    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueListResponse
     */
    public Flux<StatusValueListResponse> getStatusValue() {
        return statusValueDomainService.findAllStatus()
                .map(StatusValueListMapper.INSTANCE::statusValueListResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 상태값 관리 트리 구조 만들기
     * @return StatusValueListResponse
     */
    public Mono<List<StatusValueListResponse>> getStatusValueTree() {
        return statusValueDomainService.findAllTree()
                .map(StatusValueListMapper.INSTANCE::statusValueListResponse)
                .filter( f -> f.getParentCode() == null )
                .flatMap( res ->
                {
                    log.debug("필터 값 : {}", res.getCode());
                    res.setChildren(new ArrayList<>());
                    return statusValueDomainService.findParentCode(res.getCode()).map(
                            StatusValueListMapper.INSTANCE::statusValueListResponse
                    ).collectList()
                            .map(codeList -> {
                                res.setChildren(codeList);
                                return res;
                            });
                })
                //.collectList();
                .collectSortedList(Comparator.comparing(StatusValueListResponse::getOrder));
    }

    /**
     * 상태값 관리 1개 저장
     * @param statusValueListRequest
     * @return StatusValueListResponse
     */
    public Mono<StatusValueListResponse> create(StatusValueListRequest statusValueListRequest) {
        return statusValueDomainService.save(StatusValueListMapper.INSTANCE.statusValueRequestToStatusValueList(statusValueListRequest))
                .map(StatusValueListMapper.INSTANCE::statusValueListResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }
}
