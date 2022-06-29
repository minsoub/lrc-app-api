package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.mapper.UserAccountMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserAccountRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response.UserAccountResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserAccountDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserInfoDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountDomainService userAccountDomainService;
    private final UserInfoDomainService userInfoDomainService;

    private final AwsProperties properties;

    /**
     * 마케팅 수량 id로 찾기
     *
     * @param projectId
     * @return MarketingQuantityResponse Object
     */
    public Mono<List<UserAccountResponse>> findByProjectId(String projectId) {
        return userAccountDomainService.findByProjectId(projectId)
                .flatMap(user -> {
                    log.debug("user info => {}", user);
                    return userInfoDomainService.findById(user.getUserAccountId())
                            .flatMap(u -> {
                                return Mono.just(UserAccountResponse.builder()
                                        .id(user.getId())
                                        .userAccountId(user.getUserAccountId())
                                        .projectId(user.getProjectId())
                                        .userName(AES256Util.decryptAES(properties.getKmsKey(), u.getName()))
                                        .snsId(u.getSnsId())
                                        .email(AES256Util.decryptAES(properties.getKmsKey(), u.getEmail()))
                                        .phone(AES256Util.decryptAES(properties.getKmsKey(), u.getPhone()))
                                        .build());
                            });
                })
                //.map(UserAccountMapper.INSTANCE::userAccountResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 마케팅 수량 여러개 저장 및 업데이트
     * @param projectId
     * @param userAccountRequest
     * @return MarketingQuantityResponse Object
     */
    public Mono<List<UserAccountResponse>> create(String projectId, UserAccountRequest userAccountRequest) {
        return Mono.just(userAccountRequest.getUserLists())
                .flatMapMany(userLists -> Flux.fromIterable(userLists))
                .flatMap(userList ->
                        userAccountDomainService.save(UserAccountMapper.INSTANCE.userAccountResponseToRequest(userList))
                )
                .then(this.findByProjectId(projectId));
    }
}
