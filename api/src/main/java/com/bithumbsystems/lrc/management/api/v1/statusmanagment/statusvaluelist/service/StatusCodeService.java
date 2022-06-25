package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.mapper.StatusValueListMapper;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request.StatusModifyRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request.StatusCodeRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.response.StatusCodeResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity.StatusCode;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.service.StatusCodeDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class StatusCodeService {

    private final StatusCodeDomainService statusValueDomainService;

    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueListResponse
     */
    public Flux<StatusCodeResponse> getStatusValue() {
        return statusValueDomainService.findAllStatus()
                .map(StatusValueListMapper.INSTANCE::statusValueListResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 상태값 관리 트리 구조 만들기
     * @return StatusValueListResponse
     */
    public Mono<List<StatusCodeResponse>> getStatusValueTree() {
        return statusValueDomainService.findAllTree()
                .map(StatusValueListMapper.INSTANCE::statusValueListResponse)
                .filter( f -> f.getParentCode() == null || "".equals(f.getParentCode()) )
                .filter( f -> f.getUseYn() == true)
                .flatMap( res ->
                {
                    log.debug("필터 값 : {}", res.getId());
                    res.setChildren(new ArrayList<>());
                    return statusValueDomainService.findParentCode(res.getId())
                            .map(StatusValueListMapper.INSTANCE::statusValueListResponse)
                            .filter( f -> f.getUseYn() == true)
                            .collectSortedList(Comparator.comparing(StatusCodeResponse::getOrderNo))
                            .flatMap(codeList -> {
                                log.debug("data is exists....");
                                res.setChildren(codeList);
                                return Mono.just(res);
                            });
                })
                .collectSortedList(Comparator.comparing(StatusCodeResponse::getOrderNo));
    }

    /**
     * 상태값 관리 1개 저장 - 신규 등록
     * @param statusValueListRequest
     * @return StatusValueListResponse
     */
    public Mono<StatusCodeResponse> create(StatusCodeRequest statusValueListRequest, Account account) {

        return statusValueDomainService.save(
                StatusCode.builder()
                        .id(UUID.randomUUID().toString())
                        .name(statusValueListRequest.getName())
                        .parentCode(statusValueListRequest.getParentCode())
                        .orderNo(statusValueListRequest.getOrderNo())
                        .useYn(statusValueListRequest.getUseYn())
                        .createDate(LocalDateTime.now())
                        .createAdminAccountId(account.getAccountId())
                        .build()
        ).map(StatusValueListMapper.INSTANCE::statusValueListResponse)
        .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 상태값 관리 1개 저장 - 수정
     * @param statusValueListRequest
     * @return StatusValueListResponse
     */
    public Mono<StatusCodeResponse> update(StatusModifyRequest statusValueListRequest, Account account) {

        return statusValueDomainService.findStatusValueById(statusValueListRequest.getId())
                .flatMap(result -> {
                    result.setName(statusValueListRequest.getName());
                    result.setOrderNo(statusValueListRequest.getOrderNo());
                    result.setParentCode(statusValueListRequest.getParentCode());
                    result.setCreateDate(result.getCreateDate());
                    result.setCreateAdminAccountId(result.getCreateAdminAccountId());
                    result.setUpdateDate(LocalDateTime.now());
                    result.setUpdateAdminAccountId(account.getAccountId());
                    return statusValueDomainService.update(result)
                            .map(StatusValueListMapper.INSTANCE::statusValueListResponse);
                })
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 상태값 관리 1개 삭제
     * @param id
     * @return N/A
     */
    public Mono<Void> deleteStatusValue(String id) {
        return statusValueDomainService.findStatusValueById(id).flatMap(statusValueDomainService::deleteStatusValue);
    }
}
