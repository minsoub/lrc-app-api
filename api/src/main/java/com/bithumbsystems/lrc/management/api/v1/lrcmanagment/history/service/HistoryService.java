package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.service;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.core.util.MaskingUtil;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.mapper.HistoryMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.request.HistoryRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.response.HistoryResponse;
import com.bithumbsystems.persistence.mongodb.account.service.AccountDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service.HistoryDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserAccountDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryDomainService historyDomainService;
    private final UserAccountDomainService userAccountDomainService;
    private final AccountDomainService  adminAccountDomainService;
    private final AwsProperties awsProperties;

    /**
     * 히스토리 모두 가져오기
     * @return HistoryResponse
     */
    public Mono<List<HistoryResponse>> getHistory(String projectId, String keyword) {
        return historyDomainService.findBySearch(projectId, keyword)
                .flatMap(result -> {
                    if (result.getType() == null || result.getUpdateAccountId() == null) {
                        return Mono.just(result);
                    } else {
                        if (result.getType().equals("ADMIN")) {
                            return adminAccountDomainService.findByAdminId(result.getUpdateAccountId())
                                    .flatMap(r1 -> {
                                        result.setCustomer(MaskingUtil.getNameMask(r1.getName())+"("+ MaskingUtil.getEmailMask(result.getCustomer())+")");
                                        return Mono.just(result);
                                    });
                        } else {
                            return userAccountDomainService.findByProjectIdAndUserAccountId(projectId, result.getUpdateAccountId())
                                    .flatMap(r1 -> {
                                        String customer = (StringUtils.hasLength(r1.getName()))? MaskingUtil.getNameMask(AES256Util.decryptAES(awsProperties.getKmsKey(), r1.getName()))+"("+MaskingUtil.getEmailMask(result.getCustomer())+")" : MaskingUtil.getEmailMask(result.getCustomer());
                                        result.setCustomer(customer);
                                        return Mono.just(result);
                                    });
                        }
                    }
                })
                .filter((matchResult) -> {
                    if (!StringUtils.hasLength(keyword)) return true;
                    else {
                        if (matchResult.getMenu().indexOf(keyword) != -1 || matchResult.getSubject().indexOf(keyword) != -1
                                || matchResult.getTaskHistory().indexOf(keyword) != -1 || matchResult.getCustomer().indexOf(keyword) != -1) {
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                })
//                .filter(r -> StringUtils.hasLength(keyword) ? (r.getMenu().indexOf(keyword) == -1 ? false : true) : true)
//                .filter(r -> StringUtils.hasLength(keyword) ? (r.getSubject().indexOf(keyword) == -1 ? false : true) : true)
//                .filter(r -> StringUtils.hasLength(keyword) ? (r.getTaskHistory().indexOf(keyword) == -1 ? false : true) : true)
//                .filter(r -> StringUtils.hasLength(keyword) ? (r.getCustomer().indexOf(keyword) == -1 ? false : true) : true)
                .map(HistoryMapper.INSTANCE::historyResponse)
                .collectSortedList(Comparator.comparing(HistoryResponse::getUpdateDate).reversed());
    }

    /**
     * 히스토리 1개 저장하기
     * @return HistoryResponse
     */
    public Mono<HistoryResponse> create(HistoryRequest historyRequest){
        return historyDomainService.save(HistoryMapper.INSTANCE.historyResponseToRequest(historyRequest))
                .map(HistoryMapper.INSTANCE::historyResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }
}
