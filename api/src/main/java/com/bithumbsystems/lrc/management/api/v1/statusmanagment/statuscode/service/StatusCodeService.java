package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.exception.StatusCodeException;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.mapper.StatusCodeMapper;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.request.StatusCodeRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.request.StatusModifyRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.response.StatusCodeResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.model.entity.StatusCode;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.service.StatusCodeDomainService;
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
     * @return StatusCodeResponse
     */
    public Flux<StatusCodeResponse> getStatusValue() {
        return statusValueDomainService.findAllStatus()
                .map(StatusCodeMapper.INSTANCE::statusCodeResponse);
    }

    /**
     * 상태값 관리 트리 구조 만들기
     * @return StatusCodeResponse
     */
    public Mono<List<StatusCodeResponse>> getStatusValueTree() {
        return statusValueDomainService.findAllTree()
                .map(StatusCodeMapper.INSTANCE::statusCodeResponse)
                .filter( f -> f.getParentCode() == null || "".equals(f.getParentCode()) )
                .filter( f -> f.getUseYn())
                .flatMap( res ->
                {
                    log.debug("필터 값 : {}", res.getId());
                    res.setChildren(new ArrayList<>());
                    return statusValueDomainService.findParentCode(res.getId())
                            .map(StatusCodeMapper.INSTANCE::statusCodeResponse)
                            .filter( f -> f.getUseYn())
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
     * @param statusCodeRequest
     * @return StatusCodeResponse
     */
    public Mono<StatusCodeResponse> create(StatusCodeRequest statusCodeRequest, Account account) {

        return statusValueDomainService.save(
                StatusCode.builder()
                        .id(UUID.randomUUID().toString())
                        .name(statusCodeRequest.getName())
                        .nameEn(statusCodeRequest.getNameEn())
                        .parentCode(statusCodeRequest.getParentCode())
                        .orderNo(statusCodeRequest.getOrderNo())
                        .useYn(statusCodeRequest.getUseYn())
                        .createDate(LocalDateTime.now())
                        .createAdminAccountId(account.getAccountId())
                        .build()
        ).map(StatusCodeMapper.INSTANCE::statusCodeResponse)
        .switchIfEmpty(Mono.error(new StatusCodeException(ErrorCode.FAIL_CREATE_CONTENT)));
    }

    /**
     * 상태값 관리 1개 저장 - 수정
     * @param statusModifyRequest
     * @return StatusCodeResponse
     */
    public Mono<StatusCodeResponse> update(StatusModifyRequest statusModifyRequest, Account account) {

        return statusValueDomainService.findStatusValueById(statusModifyRequest.getId())
                .flatMap(result -> {
                    result.setName(statusModifyRequest.getName());
                    result.setNameEn(statusModifyRequest.getNameEn());
                    result.setOrderNo(statusModifyRequest.getOrderNo());
                    result.setParentCode(statusModifyRequest.getParentCode());
                    result.setCreateDate(result.getCreateDate());
                    result.setCreateAdminAccountId(result.getCreateAdminAccountId());
                    result.setUpdateDate(LocalDateTime.now());
                    result.setUpdateAdminAccountId(account.getAccountId());
                    result.setUseYn(statusModifyRequest.getUseYn());
                    return statusValueDomainService.update(result)
                            .map(StatusCodeMapper.INSTANCE::statusCodeResponse);
                })
                .switchIfEmpty(Mono.error(new StatusCodeException(ErrorCode.FAIL_UPDATE_CONTENT)));
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
