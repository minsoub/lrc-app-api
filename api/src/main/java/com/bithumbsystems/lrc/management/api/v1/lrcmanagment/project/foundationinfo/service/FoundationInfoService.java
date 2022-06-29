package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.mapper.FoundationInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.request.FoundationInfoRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.response.FoundationInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.repository.StatusCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FoundationInfoService {

    private final FoundationInfoDomainService foundationInfoDomainService;
    private final StatusCodeRepository statusCodeRepository;

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
                                        .progressCode(result.getProgressCode())
                                        .build());
                            })
                            .flatMap(res -> {
                                return statusCodeRepository.findById(res.getProgressCode())
                                        .flatMap(r2 -> {
                                            res.setProgressName(r2.getName());
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
                    c.setName(foundationInfoRequest.getProjectName());
                    c.setSymbol(foundationInfoRequest.getSymbol());
                    c.setContractCode(foundationInfoRequest.getContractCode());
                    c.setProgressCode(foundationInfoRequest.getProgressCode());
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
}
