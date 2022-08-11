package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.service;

import static com.bithumbsystems.lrc.management.api.core.util.NumberUtil.checkDecimalPoint;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.listener.HistoryDto;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.exception.MarketingQuantityException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.mapper.MarketingQuantityMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.request.MarketingQuantityRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.response.MarketingQuantityResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.model.entity.MarketingQuantity;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.service.MarketingQuantityDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Log4j2
public class MarketingQuantityService {

  private final MarketingQuantityDomainService marketingQuantityDomainService;
  private final ApplicationEventPublisher applicationEventPublisher;

  /**
   * 마케팅 수량 id로 찾기
   *
   * @param projectId
   * @return MarketingQuantityResponse Object
   */
  public Mono<List<MarketingQuantityResponse>> findByProjectId(String projectId) {
    return marketingQuantityDomainService.findByUseData(projectId)
        .map(MarketingQuantityMapper.INSTANCE::marketingQuantityResponse)
        .collectList();
  }

  /**
   * 마케팅 수량 여러개 저장 및 업데이트
   *
   * @param projectId
   * @param marketingQuantityRequest
   * @return MarketingQuantityResponse Object
   */
  @Transactional
  public Mono<List<MarketingQuantityResponse>> create(String projectId,
      MarketingQuantityRequest marketingQuantityRequest, Account account) {
    return Mono.just(marketingQuantityRequest.getMarketingList())
        .flatMapMany(Flux::fromIterable)
        .flatMap(marketing -> {
              if (StringUtils.hasLength(marketing.getId())) {
                return marketingQuantityDomainService.findById(marketing.getId())
                    .flatMap(result -> {
                      log.debug("quantity {} => {}, {}", result.getSymbol(),
                          result.getMinimumQuantity(), marketing.getMinimumQuantity());
                      log.debug("quantity {} => {}, {}", result.getSymbol(), result.getActualQuantity(),
                          marketing.getActualQuantity());
//                      if(!checkDecimalPoint(marketing.getMinimumQuantity()) || !checkDecimalPoint(marketing.getActualQuantity())) {
//                        return Mono.error(new MarketingQuantityException(ErrorCode.INVALID_NUMBER_FORMAT));
//                      }
                      if (!result.getMinimumQuantity().equals(marketing.getMinimumQuantity())) {
                        historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "최소 지원 수량", "수정", String.valueOf(marketing.getMinimumQuantity()), account);
                      }
                      if (!result.getActualQuantity().equals(marketing.getActualQuantity())) {
                        historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "실제 상장 지원 수량", "수정", String.valueOf(marketing.getActualQuantity()), account);
                      }
                      return Mono.just(result);
                    })
                    .flatMap(r -> marketingQuantityDomainService.save(
                        MarketingQuantityMapper.INSTANCE.marketingQuantityResponseToMarketingQuantity(
                            marketing)));
              } else { // 신규 등록...
                historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "최소 지원 수량", "등록", String.valueOf(marketing.getMinimumQuantity()), account);
                historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "실제 상장 지원 수량", "등록", String.valueOf(marketing.getActualQuantity()), account);
                return marketingQuantityDomainService.insert(
                    MarketingQuantityMapper.INSTANCE.marketingQuantityResponseToMarketingQuantity(
                        marketing));
              }
            }
        )
        .then(this.findByProjectId(projectId));
  }

    /**
     * 마케팅 수량을 삭제한다.
     *
     * @param projectId
     * @param id
     * @param account
     * @return
     */
  @Transactional
  public Mono<MarketingQuantityResponse> deleteById(String projectId, String id, Account account) {
      return marketingQuantityDomainService.findById(id)
              .flatMap(result -> {
                  historyLogSend(projectId, "프로젝트 관리>프로젝트 정보", "마켓팅 수량", "삭제", "", account);
                  result.setDelYn(true);
                  return marketingQuantityDomainService.save(result)
                          .flatMap(res -> Mono.just(MarketingQuantityMapper.INSTANCE.marketingQuantityResponse(res)));
              });
  }

  /**
   * 변경 히스토리 저장.
   *
   * @param projectId
   * @param menu
   * @param subject
   * @param taskHistory
   * @param account
   * @return
   */
  private void historyLogSend(String projectId, String menu, String subject, String taskHistory, String item,
      Account account) {
    applicationEventPublisher.publishEvent(
        HistoryDto.builder()
            .projectId(projectId)
            .menu(menu)
            .subject(subject)
            .taskHistory(taskHistory)
            .item(item)
            .email(account.getEmail())
            .accountId(account.getAccountId())
            .build()
    );
  }
}
