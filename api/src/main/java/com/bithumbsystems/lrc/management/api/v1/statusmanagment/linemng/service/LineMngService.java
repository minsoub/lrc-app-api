package com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.exception.LineMngException;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.mapper.LineMngMapper;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.request.LineMngRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.response.LineMngResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity.LineMng;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service.LineMngDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LineMngService {

    private final LineMngDomainService businessListDomainService;

    /**
     * 계열관리 모든 정보
     * @return BusinessListResponse
     */
    public Flux<LineMngResponse> findAll(LineType type) {
        return businessListDomainService.findAll()
                .filter(f -> type == null || "".equals(type) || f.getType().equals(type))
                .filter(LineMng::getUseYn)
                .map(LineMngMapper.INSTANCE::businessListResponse)
                .switchIfEmpty(Mono.error(new LineMngException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 계열관리 1개 저장
     * @param lineMngRequest
     * @return BusinessListResponse
     */
    public Mono<LineMngResponse> create(LineMngRequest lineMngRequest, Account account) {
        return businessListDomainService.save(LineMng.builder()
                        .id(UUID.randomUUID().toString())
                        .name(lineMngRequest.getName())
                        .type(lineMngRequest.getType())
                        .useYn(true)
                        .createDate(LocalDateTime.now())
                        .createAdminAccountId(account.getAccountId()).build())
                .map(LineMngMapper.INSTANCE::businessListResponse)
                .switchIfEmpty(Mono.error(new LineMngException(ErrorCode.FAIL_CREATE_CONTENT)));
    }

    /**
     * 계열관리 업데이트
     * @param id
     * @param lineMngRequest
     * @return BusinessListResponse
     */
    public Mono<LineMngResponse> updateLine(String id, LineMngRequest lineMngRequest, Account account) {
        return businessListDomainService.findById(id).flatMap(c -> {
            c.setName(lineMngRequest.getName());
            c.setUpdateDate(LocalDateTime.now());
            c.setUpdateAdminAccountId(account.getAccountId());
            return businessListDomainService.updateLine(c)
                    .map(LineMngMapper.INSTANCE::businessListResponse);
        })
                .switchIfEmpty(Mono.error(new LineMngException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * 계열관리 삭제 - 상태 여부만 변경.
     * @param id
     * @return null
     */
    public Mono<LineMngResponse> deleteLine(String id, Account account) {
        return businessListDomainService.findById(id).flatMap(c -> {
                    c.setUseYn(false);  // 상태 여부만 변경
                    c.setUpdateDate(LocalDateTime.now());
                    c.setUpdateAdminAccountId(account.getAccountId());
                    return businessListDomainService.updateLine(c)
                            .map(LineMngMapper.INSTANCE::businessListResponse);
                })
                .switchIfEmpty(Mono.error(new LineMngException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }
}
