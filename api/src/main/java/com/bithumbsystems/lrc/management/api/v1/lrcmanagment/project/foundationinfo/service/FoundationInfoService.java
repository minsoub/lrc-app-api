package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.mapper.FoundationInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.request.FoundationInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.response.FoundationInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.listener.HistoryDto;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service.HistoryDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.repository.StatusCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FoundationInfoService {

    private final FoundationInfoDomainService foundationInfoDomainService;
    private final StatusCodeRepository statusCodeRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    /**
     * 재단 정보 1개 id 찾기
     *
     * @param id
     * @return FoundationInfoResponse Object
     */
    public Mono<FoundationInfoResponse> findById(String id) {
        return foundationInfoDomainService.findById(id)
                .flatMap(result -> {
                    return statusCodeRepository.findById(result.getContractCode())
                            .flatMap(r1 -> {
                                return Mono.just(FoundationInfoResponse.builder()
                                        .id(result.getId())
                                        .adminMemo(result.getMemo())
                                        .symbol(result.getSymbol())
                                        .projectName(result.getName())
                                        .contractCode(result.getContractCode())
                                        .contractName(r1.getName())
                                        .processCode(result.getProcessCode())
                                        .build());
                            })
                            .flatMap(res -> {
                                return statusCodeRepository.findById(res.getProcessCode())
                                        .flatMap(r2 -> {
                                            res.setProcessName(r2.getName());
                                            return Mono.just(res);
                                        });
                            });
                })
                //.map(FoundationInfoMapper.INSTANCE::foundationInfoResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 프로젝트 정보 업데이트
     *
     * @param foundationInfoRequest
     * @return FoundationInfoResponse Object
     */
    public Mono<FoundationInfoResponse> updateFoundationInfo(String id, FoundationInfoRequest foundationInfoRequest, Account account) {
        return foundationInfoDomainService.findById(id)
                .flatMap(c -> {
                    // 변경 히스토리 추가
                    if (!c.getName().equals(foundationInfoRequest.getProjectName())) {
                        historyLogSend(id, "프로젝트 관리>재단정보", "프로젝트명", "수정", account);
                    }
                    if (!c.getSymbol().equals(foundationInfoRequest.getSymbol())) {
                        historyLogSend(id, "프로젝트 관리>재단정보", "심볼", "수정", account);
                    }
                    if (!c.getContractCode().equals(foundationInfoRequest.getContractCode())) {
                        historyLogSend(id, "프로젝트 관리>재단정보", "계약상태", "상태변경", account);
                    }
                    if (!c.getProcessCode().equals(foundationInfoRequest.getProcessCode())) {
                        historyLogSend(id, "프로젝트 관리>재단정보", "진행상태", "상태변경", account);
                    }
                    if (!c.getMemo().equals(foundationInfoRequest.getAdminMemo())) {
                        historyLogSend(id, "프로젝트 관리>재단정보", "관리자 메모", "수정", account);
                    }
                    c.setName(foundationInfoRequest.getProjectName());
                    c.setSymbol(foundationInfoRequest.getSymbol());
                    c.setContractCode(foundationInfoRequest.getContractCode());
                    c.setProcessCode(foundationInfoRequest.getProcessCode());
                    c.setMemo(foundationInfoRequest.getAdminMemo());
                    c.setUpdateDate(LocalDateTime.now());
                    c.setUpdateAdminAccountId(account.getAccountId());

                    return foundationInfoDomainService.updateFoundationInfo(c)
                            .map(FoundationInfoMapper.INSTANCE::foundationInfoResponse);
                })
                .switchIfEmpty(
                        foundationInfoDomainService.updateFoundationInfo(FoundationInfoMapper.INSTANCE.foundationInfoRequestToFoundationInfo(foundationInfoRequest))
                                .map(FoundationInfoMapper.INSTANCE::foundationInfoResponse)
                                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)))
                        //
                );
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
