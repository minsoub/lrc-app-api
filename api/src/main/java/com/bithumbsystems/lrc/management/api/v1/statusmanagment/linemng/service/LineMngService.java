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
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Line mng service.
 */
@Service
@RequiredArgsConstructor
@Validated
public class LineMngService {

  private final LineMngDomainService businessListDomainService;

  /**
   * 계열관리 모든 정보.
   *
   * @param type the type
   * @return BusinessListResponse flux
   */
  public Flux<LineMngResponse> findAll(LineType type) {
    return businessListDomainService.findAll()
        .filter(f -> type == null || f.getType().equals(type))
        .filter(f -> f.getDelYn() == null || f.getDelYn().equals(false))
        .map(LineMngMapper.INSTANCE::businessListResponse);
  }

  /**
   * 계열관리 트리 구조 만들기.
   *
   * @param type  the type
   * @param isUse the is use
   * @return the mono
   */
  public Mono<List<LineMngResponse>> findAllTree(LineType type, Boolean isUse) {
    return businessListDomainService.findAll()
        .filter(f -> type == null || f.getType().equals(type))
        .filter(f -> f.getDelYn() == null || f.getDelYn().equals(false))
        .filter(f -> StringUtils.isEmpty(f.getParentId()))
        .map(LineMngMapper.INSTANCE::businessListResponse)
        .flatMap(res -> businessListDomainService.findByParentId(res.getId())
            .map(LineMngMapper.INSTANCE::businessListResponse)
            .filter(f -> isUse || f.getUseYn())
            .collectSortedList(Comparator.comparing(LineMngResponse::getOrderNo))
            .flatMap(lineList -> Mono.just(res.setChildren(lineList)))
        )
        .collectSortedList(Comparator.comparing(LineMngResponse::getType).thenComparing(LineMngResponse::getOrderNo));
  }

  /**
   * 계열관리 1개 저장.
   *
   * @param lineMngRequest the line mng request
   * @param account        the account
   * @return BusinessListResponse mono
   */
  @Validated(LineMngRequest.OnCreate.class)
  public Mono<LineMngResponse> create(@Valid LineMngRequest lineMngRequest, Account account) {
    return businessListDomainService.save(LineMng.builder()
            .id(UUID.randomUUID().toString())
            .name(lineMngRequest.getName())
            .type(LineType.valueOf(lineMngRequest.getType()))
            .orderNo(lineMngRequest.getOrderNo())
            .parentId(lineMngRequest.getParentId())
            .useYn(Boolean.valueOf(lineMngRequest.getUseYn()))
            .delYn(false)
            .createDate(LocalDateTime.now())
            .createAdminAccountId(account.getAccountId()).build())
        .map(LineMngMapper.INSTANCE::businessListResponse)
        .switchIfEmpty(Mono.error(new LineMngException(ErrorCode.FAIL_CREATE_CONTENT)));
  }

  /**
   * 계열관리 업데이트.
   *
   * @param id             the id
   * @param lineMngRequest the line mng request
   * @param account        the account
   * @return BusinessListResponse mono
   */
  @Validated(LineMngRequest.OnUpdate.class)
  public Mono<LineMngResponse> updateLine(String id, @Valid LineMngRequest lineMngRequest, Account account) {
    return businessListDomainService.findById(id).flatMap(c -> {
      c.setName(lineMngRequest.getName());
      c.setOrderNo(lineMngRequest.getOrderNo());
      c.setUseYn(Boolean.valueOf(lineMngRequest.getUseYn()));
      c.setUpdateDate(LocalDateTime.now());
      c.setUpdateAdminAccountId(account.getAccountId());
      return businessListDomainService.updateLine(c)
          .map(LineMngMapper.INSTANCE::businessListResponse);
    })
        .switchIfEmpty(Mono.error(new LineMngException(ErrorCode.FAIL_UPDATE_CONTENT)));
  }

  /**
   * 계열관리 삭제 - 상태 여부만 변경.
   *
   * @param id      the id
   * @param account the account
   * @return null mono
   */
  public Mono<LineMngResponse> deleteLine(String id, Account account) {
    return businessListDomainService.findById(id).flatMap(c -> {
      c.setUseYn(false);
      c.setDelYn(true);
      c.setUpdateDate(LocalDateTime.now());
      c.setUpdateAdminAccountId(account.getAccountId());
      return businessListDomainService.updateLine(c)
          .map(LineMngMapper.INSTANCE::businessListResponse);
    })
        .switchIfEmpty(Mono.error(new LineMngException(ErrorCode.FAIL_UPDATE_CONTENT)));
  }
}