package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryLog;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.exception.FoundationInfoException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.mapper.FoundationInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.request.FoundationInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.response.CheckProjectResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.response.FoundationInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.exception.ProjectInfoException;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.repository.StatusCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FoundationInfoService {

    private final FoundationInfoDomainService foundationInfoDomainService;
    private final StatusCodeRepository statusCodeRepository;

    private final HistoryLog historyLog;

    /**
     * 재단 정보 1개 id 찾기
     *
     * @param id
     * @return FoundationInfoResponse Object
     */
    public Mono<FoundationInfoResponse> findById(String id) {
        return foundationInfoDomainService.findById(id)
                .flatMap(result -> {
                    if (result.getContractCode() == null) {
                        return Mono.just(FoundationInfoResponse.builder()
                                .id(result.getId())
                                .adminMemo(result.getMemo())
                                .symbol(result.getSymbol())
                                .projectName(result.getName())
                                .contractCode(result.getContractCode())
                                .contractName("")
                                .processCode(result.getProcessCode())
                                .build());
                    } else {
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
                                .switchIfEmpty(
                                        Mono.just(FoundationInfoResponse.builder()
                                                .id(result.getId())
                                                .adminMemo(result.getMemo())
                                                .symbol(result.getSymbol())
                                                .projectName(result.getName())
                                                .contractCode(result.getContractCode())
                                                .contractName("")
                                                .processCode(result.getProcessCode())
                                                .build())
                                );
                    }
                })
                .flatMap(res -> {
                    if (res.getProcessCode() == null) {
                        return Mono.just(res);
                    }else {
                        return statusCodeRepository.findById(res.getProcessCode())
                                .flatMap(r2 -> {
                                    res.setProcessName(r2.getName());
                                    return Mono.just(res);
                                })
                                .switchIfEmpty(
                                       Mono.just(res)
                                );
                    }
                });
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
                        historyLog.send(id, "프로젝트 관리>재단정보", "프로젝트명", "수정", foundationInfoRequest.getProjectName(), account);
                    }
                    if (!c.getSymbol().equals(foundationInfoRequest.getSymbol())) {
                        historyLog.send(id, "프로젝트 관리>재단정보", "심볼", "수정", foundationInfoRequest.getSymbol(), account);
                    }
                    if ((c.getContractCode() == null && foundationInfoRequest.getContractCode() != null) || !c.getContractCode().equals(foundationInfoRequest.getContractCode())) {
                        statusCodeRepository.findById(foundationInfoRequest.getContractCode())
                                        .flatMap(res -> {
                                            historyLog.send(id, "프로젝트 관리>재단정보", "계약상태", "상태변경", res.getName(), account); // foundationInfoRequest.getContractCode(), account);
                                            return Mono.just(res);
                                        }).subscribe();

                    }
                    if ((c.getProcessCode() == null && foundationInfoRequest.getProcessCode() != null) ||  !c.getProcessCode().equals(foundationInfoRequest.getProcessCode())) {
                        statusCodeRepository.findById(foundationInfoRequest.getProcessCode())
                                        .flatMap(res -> {
                                            historyLog.send(id, "프로젝트 관리>재단정보", "진행상태", "상태변경", res.getName(), account); // foundationInfoRequest.getProcessCode(),  account);
                                            return Mono.just(res);
                                        }).subscribe();

                    }
                    if (c.getMemo() == null && foundationInfoRequest.getAdminMemo() == null) {
                        // NOT
                    } else if ((c.getMemo() == null && foundationInfoRequest.getAdminMemo() != null) || !c.getMemo().equals(foundationInfoRequest.getAdminMemo())) {
                        historyLog.send(id, "프로젝트 관리>재단정보", "관리자 메모", "수정", foundationInfoRequest.getAdminMemo(), account);
                    }
                    c.setName(foundationInfoRequest.getProjectName());
                    c.setSymbol(foundationInfoRequest.getSymbol());
                    c.setContractCode(foundationInfoRequest.getContractCode());
                    c.setProcessCode(foundationInfoRequest.getProcessCode());
                    c.setMemo(foundationInfoRequest.getAdminMemo());
                    c.setUpdateDate(LocalDateTime.now());
                    c.setUpdateAccountId(account.getAccountId());

                    return foundationInfoDomainService.updateFoundationInfo(c)
                            .map(FoundationInfoMapper.INSTANCE::foundationInfoResponse);
                })
                .switchIfEmpty(
                        foundationInfoDomainService.updateFoundationInfo(FoundationInfoMapper.INSTANCE.foundationInfoRequestToFoundationInfo(foundationInfoRequest))
                                .map(FoundationInfoMapper.INSTANCE::foundationInfoResponse)
                                .switchIfEmpty(Mono.error(new FoundationInfoException(ErrorCode.FAIL_UPDATE_CONTENT)))
                        //
                );
    }
    /**
     * 프로젝트 링크 시 프로젝트 존재여부 체크
     * @param projectId 프로젝트 아이디
     * @return CheckProjectResponse 성공여부
     */
    public Mono<CheckProjectResponse> checkProject(String projectId) {
        return foundationInfoDomainService.findById(projectId)
                .switchIfEmpty(Mono.error(new ProjectInfoException(ErrorCode.INVALID_PROJECT)))
                .flatMap(projectInfo -> {
                    return Mono.just(CheckProjectResponse.builder().result(true).build());
                });
    }

    /**
     * 프로젝트 이름 중복 체크
     * @param foundationInfoRequest
     * @return
     */
    public Mono<Boolean> checkProjectName(FoundationInfoRequest foundationInfoRequest) {
        return foundationInfoDomainService.getByName(foundationInfoRequest.getProjectName()).flatMap(flag -> {
            if (flag) {
                return foundationInfoDomainService.findById(foundationInfoRequest.getId()).flatMap(c -> {
                    if (c.getName().equals(foundationInfoRequest.getProjectName())) {
                        return Mono.just(true);
                    }
                    return Mono.error(new ProjectInfoException(ErrorCode.PROJECT_NAME_DUPLICATE));
                });
            } else {
                return Mono.just(true);
            }
        });
    }
}
