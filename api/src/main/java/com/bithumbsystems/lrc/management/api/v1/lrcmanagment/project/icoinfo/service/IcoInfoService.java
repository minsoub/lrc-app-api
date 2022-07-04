package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.exception.IcoInfoException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.mapper.IcoInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.request.IcoInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.response.IcoInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.listener.HistoryDto;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service.HistoryDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.service.IcoInfoDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IcoInfoService {

    private final IcoInfoDomainService icoInfoDomainService;
    private final ApplicationEventPublisher applicationEventPublisher;
    /**
     * 상장 정보 1개 id 찾기
     *
     * @param projectId
     * @return IcoInfoResponse Object
     */
    public Mono<List<IcoInfoResponse>> findByProjectId(String projectId) {
        return icoInfoDomainService.findByProjectId(projectId)
                .map(IcoInfoMapper.INSTANCE::icoInfoResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new IcoInfoException(ErrorCode.NOT_FOUND_CONTENT)));

    }

    /**
     * 상장 정보 여러개 저장 및 업데이트
     *
     * @param icoInfoRequest
     * @return IcoInfoResponse Object
     */
    public Mono<List<IcoInfoResponse>> create(String projectId, IcoInfoRequest icoInfoRequest, Account account) {
        return Mono.just(icoInfoRequest.getIcoInfoList())
                .flatMapMany(icoInfos -> Flux.fromIterable(icoInfos))
                .flatMap(icoInfo -> {
                                return icoInfoDomainService.findById(icoInfo.getId())
                                        .flatMap(result -> {
                                            if (icoInfo.getMarketInfo().equals("KRW")) {
                                                if (result.getPrice() != icoInfo.getPrice()) {
                                                    historyLogSend(projectId, "프로젝트 관리>상장 정보", "KRW 상장가", "수정", account);
                                                }
                                                if (result.getIcoDate() != icoInfo.getIcoDate()) {
                                                    historyLogSend(projectId, "프로젝트 관리>상장 정보", "KRW 상장일", "수정", account);
                                                }
                                            }else {
                                                if (result.getPrice() != icoInfo.getPrice()) {
                                                    historyLogSend(projectId, "프로젝트 관리>상장 정보", "BTC 상장가", "수정", account);
                                                }
                                                if (result.getIcoDate() != icoInfo.getIcoDate()) {
                                                    historyLogSend(projectId, "프로젝트 관리>상장 정보", "BTC 상장일", "수정", account);
                                                }
                                            }
                                            return  Mono.just(result);
                                        })
                                        .flatMap(res ->icoInfoDomainService.save(IcoInfoMapper.INSTANCE.icoInfoRequestToIcoInfo(icoInfo)));
                        }
                )
                .then(this.findByProjectId(projectId));
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
    private void historyLogSend(String projectId, String menu, String subject, String taskHistory, Account account) {
        applicationEventPublisher.publishEvent(
                HistoryDto.builder()
                        .projectId(projectId)
                        .menu(menu)
                        .subject(subject)
                        .taskHistory(taskHistory)
                        .email(account.getEmail())
                        .accountId(account.getAccountId())
                        .build()
        );
    }
}
