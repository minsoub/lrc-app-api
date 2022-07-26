package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.mapper.UserAccountMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserAccountRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response.UserAccountResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserAccountDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserInfoDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
                                return Mono.just(UserAccountResponse.builder()
                                        .id(user.getId())
                                        .userAccountId(user.getUserAccountId())
                                        .projectId(user.getProjectId())
                                        .userName(AES256Util.decryptAES(properties.getKmsKey(), user.getName()))
                                        .snsId(user.getSnsId())
                                        .email(AES256Util.decryptAES(properties.getKmsKey(), user.getContactEmail()))
                                        .phone(AES256Util.decryptAES(properties.getKmsKey(), user.getPhone()))
                                        .build());
                })
                .collectList();
    }

    /**
     * 마케팅 수량 여러개 저장 및 업데이트
     * @param projectId
     * @param userAccountRequest
     * @return MarketingQuantityResponse Object
     */
    public Mono<List<UserAccountResponse>> create(String projectId, UserAccountRequest userAccountRequest) {
        return Mono.just(userAccountRequest.getUserLists())
                .flatMapMany(Flux::fromIterable)
                .flatMap(userList ->
                        userAccountDomainService.save(UserAccountMapper.INSTANCE.userAccountResponseToRequest(userList))
                )
                .then(this.findByProjectId(projectId));
    }
}
