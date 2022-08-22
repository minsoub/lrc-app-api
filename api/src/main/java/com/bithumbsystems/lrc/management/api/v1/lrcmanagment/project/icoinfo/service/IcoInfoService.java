package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryLog;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.mapper.IcoInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.request.IcoInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.response.IcoInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryDto;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.model.entity.IcoInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.service.IcoInfoDomainService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class IcoInfoService {

  private final IcoInfoDomainService icoInfoDomainService;
  private final HistoryLog historyLog;

  /**
   * 상장 정보 1개 id 찾기
   *
   * @param projectId
   * @return IcoInfoResponse Object
   */
  public Mono<List<IcoInfoResponse>> findByProjectId(String projectId) {
    return icoInfoDomainService.findByProjectId(projectId)
        .map(IcoInfoMapper.INSTANCE::icoInfoResponse)
        .collectList();
  }

  /**
   * 상장 정보 여러개 저장 및 업데이트
   *
   * @param icoInfoRequest
   * @return IcoInfoResponse Object
   */
  @Transactional
  public Mono<List<IcoInfoResponse>> create(String projectId, IcoInfoRequest icoInfoRequest,
      Account account) {
    return Mono.just(icoInfoRequest.getIcoInfoList())
        .flatMapMany(Flux::fromIterable)
        .flatMap(icoInfo -> icoInfoDomainService.findById(icoInfo.getId())
            .flatMap(result -> {
//              if(!checkDecimalPoint(result.getPrice())) {
//                return Mono.error(new IcoInfoException(ErrorCode.INVALID_NUMBER_FORMAT));
//              }
              if (icoInfo.getMarketInfo().equals("KRW")) {


                if (!Objects.equals(result.getPrice(), icoInfo.getPrice())) {
                  historyLog.send(projectId, "프로젝트 관리>상장 정보", "KRW 상장가", "수정", String.valueOf(icoInfo.getPrice()), account);
                }


                if (result.getIcoDate() == null && icoInfo.getIcoDate() != null) {
                  historyLog.send(projectId, "프로젝트 관리>상장 정보", "KRW 상장일", "수정", icoInfo.getIcoDate().toString(), account);
                } else if(result.getIcoDate() != null && icoInfo.getIcoDate() == null) {
                  historyLog.send(projectId, "프로젝트 관리>상장 정보", "KRW 상장일", "수정", "", account);
                } else {
                  if (result.getIcoDate() ==  null && icoInfo.getIcoDate() == null) {
                    // nothing..
                  } else if (result.getIcoDate() != icoInfo.getIcoDate()) {
                    historyLog.send(projectId, "프로젝트 관리>상장 정보", "KRW 상장일", "수정", icoInfo.getIcoDate().toString(), account);
                  }
                }

//                if (result.getIcoDate() != icoInfo.getIcoDate()) {
//                  historyLogSend(projectId, "프로젝트 관리>상장 정보", "KRW 상장일", "수정", icoInfo.getIcoDate().toString(), account);
//                }
              } else {
                if (!Objects.equals(result.getPrice(), icoInfo.getPrice())) {
                  historyLog.send(projectId, "프로젝트 관리>상장 정보", "BTC 상장가", "수정", String.valueOf(icoInfo.getPrice()), account);
                }


                if (result.getIcoDate() == null && icoInfo.getIcoDate() != null) {
                  historyLog.send(projectId, "프로젝트 관리>상장 정보", "BTC 상장일", "수정", icoInfo.getIcoDate().toString(), account);
                } else if(result.getIcoDate() != null && icoInfo.getIcoDate() == null) {
                  historyLog.send(projectId, "프로젝트 관리>상장 정보", "BTC 상장일", "수정", "", account);
                } else {
                  if (result.getIcoDate() ==  null && icoInfo.getIcoDate() == null) {
                    // nothing..
                  } else if (result.getIcoDate() != icoInfo.getIcoDate()) {
                    historyLog.send(projectId, "프로젝트 관리>상장 정보", "BTC 상장일", "수정", icoInfo.getIcoDate().toString(), account);
                  }
                }
              }
              return icoInfoDomainService.save(
                  IcoInfoMapper.INSTANCE.icoInfoRequestToIcoInfo(icoInfo));
            })
            .switchIfEmpty(Mono.defer(() -> {
              log.debug("ico insert mode => ");
              if (icoInfo.getMarketInfo().equals("KRW")) {
                historyLog.send(projectId, "프로젝트 관리>상장 정보", "KRW 상장가", "등록", String.valueOf(icoInfo.getPrice()), account);
                historyLog.send(projectId, "프로젝트 관리>상장 정보", "KRW 상장일", "등록", icoInfo.getIcoDate().toString(), account);
              } else {
                historyLog.send(projectId, "프로젝트 관리>상장 정보", "BTC 상장가", "등록", String.valueOf(icoInfo.getPrice()), account);
                historyLog.send(projectId, "프로젝트 관리>상장 정보", "BTC 상장일", "등록", icoInfo.getIcoDate().toString(), account);
              }
              return icoInfoDomainService.insert(
                  IcoInfo.builder()
                      .id(UUID.randomUUID().toString())
                      .projectId(icoInfo.getProjectId())
                      .marketInfo(icoInfo.getMarketInfo())
                      .price(icoInfo.getPrice())
                      .icoDate(icoInfo.getIcoDate())
                      .build()
              );
            }))
        )
        .then(this.findByProjectId(projectId));
  }
}
