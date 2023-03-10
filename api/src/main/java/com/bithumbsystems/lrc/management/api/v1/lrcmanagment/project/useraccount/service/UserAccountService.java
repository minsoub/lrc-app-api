package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.service;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.core.util.MaskingUtil;
import com.bithumbsystems.lrc.management.api.v1.accesslog.request.AccessLogRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryLog;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.mapper.UserAccountMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserAccountRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserSaveRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response.UserAccountResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response.UserInfoResponse;
import com.bithumbsystems.persistence.mongodb.accesslog.model.enums.ActionType;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserAccount;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserAccountDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserInfoDomainService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserAccountService {

    private final UserAccountDomainService userAccountDomainService;
    private final UserInfoDomainService userInfoDomainService;
    private final FoundationInfoDomainService foundationInfoDomainService;

    private final ApplicationEventPublisher applicationEventPublisher;

    private final AwsProperties properties;
    private final HistoryLog historyLog;

    /**
     *  ???????????? ???????????? ????????? ????????? ??????
     *
     * @param projectId
     * @return UserAccountResponse Object
     */
    public Mono<List<UserAccountResponse>> findByProjectId(String projectId) {
        return userAccountDomainService.findByProjectId(projectId)
                .flatMap(user -> {
                    return userInfoDomainService.findById(user.getUserAccountId())
                                    .flatMap(res -> {
                                        return Mono.just(UserAccountResponse.builder()
                                                .userEmail(MaskingUtil.getEmailMask(AES256Util.decryptAES(properties.getKmsKey(), res.getEmail())))
                                                .id(user.getId())
                                                .userAccountId(user.getUserAccountId())
                                                .projectId(user.getProjectId())
                                                .userName(MaskingUtil.getNameMask(AES256Util.decryptAES(properties.getKmsKey(), user.getName())))
                                                .snsId(AES256Util.decryptAES(properties.getKmsKey(), user.getSnsId()))
                                                .email(MaskingUtil.getEmailMask(AES256Util.decryptAES(properties.getKmsKey(), user.getContactEmail())))
                                                .phone(MaskingUtil.getPhoneMask(AES256Util.decryptAES(properties.getKmsKey(), user.getPhone())))
                                                .userType(user.getUserType())
                                                .build());
                                    });
                })
                .collectList();
    }

    /**
     * ????????? ?????? ????????? ????????????.
     *
     * @param projectId
     * @param reason
     * @param account
     * @return
     */
    public Mono<List<UserAccountResponse>> unMaskfindByProjectId(String projectId, String reason, Account account) {
        // ???????????? ?????? ????????? ????????? ??????.
        // log ??????
        String title = "???????????? ????????? ?????? ??????";
        applicationEventPublisher.publishEvent(
                AccessLogRequest.builder()
                        .accountId(account.getAccountId())
                        .actionType(ActionType.VIEW)
                        .reason(reason)
                        .email(account.getEmail())
                        .description(title)
                        .siteId(account.getMySiteId())
                        .ip(account.getUserIp())
                        .build()
        );
        return userAccountDomainService.findByProjectId(projectId)
                .flatMap(user -> {
                    return userInfoDomainService.findById(user.getUserAccountId())
                            .flatMap(res -> {
                                return Mono.just(UserAccountResponse.builder()
                                        .userEmail(AES256Util.decryptAES(properties.getKmsKey(), res.getEmail()))
                                        .id(user.getId())
                                        .userAccountId(user.getUserAccountId())
                                        .projectId(user.getProjectId())
                                        .userName(AES256Util.decryptAES(properties.getKmsKey(), user.getName()))
                                        .snsId(AES256Util.decryptAES(properties.getKmsKey(), user.getSnsId()))
                                        .email(AES256Util.decryptAES(properties.getKmsKey(), user.getContactEmail()))
                                        .phone(AES256Util.decryptAES(properties.getKmsKey(), user.getPhone()))
                                        .userType(user.getUserType())
                                        .build());
                            });
                })
                .collectList();
    }
    /**
     * ????????? ????????? ????????????.
     * @param projectId
     * @return
     */
    public Mono<UserAccountResponse> findCreateUserByProjectId(String projectId) {
        return foundationInfoDomainService.findById(projectId)
                .flatMap(result -> {
                    return userInfoDomainService.findById(result.getCreateAccountId())
                            .flatMap(user -> {
                                return Mono.just(
                                        UserAccountResponse.builder()
                                                .id(user.getId())
                                                .userAccountId(user.getId())
                                                .projectId(projectId)
                                                .userName("")
                                                .phone("")
                                                .email(AES256Util.decryptAES(properties.getKmsKey(), user.getEmail()))
                                                .snsId("")
                                                .build()
                                );
                            });
                });
    }

    /**
     * ????????? ?????? ?????? ??????
     * @param projectId
     * @param userAccountRequest
     * @return MarketingQuantityResponse Object
     */
    public Mono<UserAccountResponse> create(String projectId, UserAccountRequest userAccountRequest, Account account) {

        return userInfoDomainService.findById(userAccountRequest.getId())
                .flatMap(result -> {
                    return userAccountDomainService.save(
                      UserAccount.builder()
                              .id(UUID.randomUUID().toString())
                              .userAccountId(userAccountRequest.getId())
                              .contactEmail(result.getEmail())
                              .name("")
                              .phone("")
                              .snsId("")
                              .userType("MANAGER")
                              .projectId(projectId)
                              .createAccountId(account.getAccountId())
                              .createDate(LocalDateTime.now())
                              .build()
                    ).flatMap(res -> {
                        historyLog.send(projectId, "???????????? ??????>????????? ??????", "?????????", "????????? ??????", AES256Util.decryptAES( properties.getKmsKey(), result.getEmail()), account);

//                        return Mono.just(UserAccountResponse.builder()
//                                .userEmail(MaskingUtil.getEmailMask(AES256Util.decryptAES(properties.getKmsKey(), res.getContactEmail())))
//                                .id(res.getId())
//                                .userAccountId(res.getUserAccountId())
//                                .projectId(res.getProjectId())
//                                .userName(MaskingUtil.getNameMask(AES256Util.decryptAES(properties.getKmsKey(), res.getName())))
//                                .snsId(AES256Util.decryptAES(properties.getKmsKey(), res.getSnsId()))
//                                .email(MaskingUtil.getEmailMask(AES256Util.decryptAES(properties.getKmsKey(), res.getContactEmail())))
//                                .phone(MaskingUtil.getPhoneMask(AES256Util.decryptAES(properties.getKmsKey(), res.getPhone())))
//                                .userType(res.getUserType())
//                                .build());
                        return Mono.just(UserAccountMapper.INSTANCE.userAccountResponse(res));
                    });
                });
    }

    /**
     * ????????? ????????? ????????????.
     *
     * @param projectId
     * @param userSaveRequest
     * @param account
     * @return
     */
    public Mono<List<UserAccountResponse>> save(String projectId, UserSaveRequest userSaveRequest, Account account) {
        return Flux.fromIterable(userSaveRequest.getSendData())
                .flatMap(result -> {
                    return userAccountDomainService.findById(result.getId())
                            .flatMap(res -> {
                                String email = AES256Util.decryptAES( properties.getCryptoKey(), result.getEmail());
                                String name = AES256Util.decryptAES(properties.getCryptoKey(), result.getUserName());
                                String phone = AES256Util.decryptAES(properties.getCryptoKey(), result.getPhone());
                                String sns_id = AES256Util.decryptAES(properties.getCryptoKey(), result.getSnsId());

                                res.setContactEmail(AES256Util.encryptAES(properties.getKmsKey(), email));
                                res.setName(AES256Util.encryptAES(properties.getKmsKey(), name));
                                res.setPhone(AES256Util.encryptAES(properties.getKmsKey(), phone));
                                res.setSnsId(AES256Util.encryptAES(properties.getKmsKey(), sns_id));
                                res.setUpdateAccountId(account.getAccountId());
                                res.setUpdateDate(LocalDateTime.now());

                                return userAccountDomainService.save(res)
                                        .flatMap(r -> {
                                            if (StringUtils.hasLength(result.getUserName()))
                                                historyLog.send(projectId, "???????????? ??????>????????? ??????", "??????", "??????", name, account);
                                            if (StringUtils.hasLength(result.getPhone()))
                                                historyLog.send(projectId, "???????????? ??????>????????? ??????", "?????????", "??????", phone, account);
                                            if (StringUtils.hasLength(result.getSnsId()))
                                                historyLog.send(projectId, "???????????? ??????>????????? ??????", "SNS ID", "??????", sns_id, account);
                                            if (StringUtils.hasLength(result.getEmail()))
                                                historyLog.send(projectId, "???????????? ??????>????????? ??????", "????????? ??????", "??????", email, account);

//                                            return Mono.just(UserAccountResponse.builder()
//                                                    .userEmail(MaskingUtil.getEmailMask(AES256Util.decryptAES(properties.getKmsKey(), r.getContactEmail())))
//                                                    .id(r.getId())
//                                                    .userAccountId(r.getUserAccountId())
//                                                    .projectId(r.getProjectId())
//                                                    .userName(MaskingUtil.getNameMask(AES256Util.decryptAES(properties.getKmsKey(), r.getName())))
//                                                    .snsId(AES256Util.decryptAES(properties.getKmsKey(), r.getSnsId()))
//                                                    .email(MaskingUtil.getEmailMask(AES256Util.decryptAES(properties.getKmsKey(), r.getContactEmail())))
//                                                    .phone(MaskingUtil.getPhoneMask(AES256Util.decryptAES(properties.getKmsKey(), r.getPhone())))
//                                                    .userType(r.getUserType())
//                                                    .build());

                                            return Mono.just(UserAccountMapper.INSTANCE.userAccountResponse(r));
                                        });
                            });
                })
                .collectList();
    }

    /**
     * ?????? ?????????????????? ????????? ?????? ??????
     * @param projectId
     * @param userId
     * @param account
     * @return
     */
    public Mono<UserAccountResponse> deleteUser(String projectId, String userId, Account account) {
        return userAccountDomainService.findById(userId)
                .flatMap(result -> {
                    historyLog.send(projectId, "???????????? ??????>????????? ??????", "?????????", "????????? ??????", AES256Util.decryptAES( properties.getKmsKey(), result.getContactEmail()), account);
                    return userAccountDomainService.delete(result)
                            .then(Mono.just(UserAccountResponse.builder()
                                    .userEmail(MaskingUtil.getEmailMask(AES256Util.decryptAES(properties.getKmsKey(), result.getContactEmail())))
                                    .id(result.getId())
                                    .userAccountId(result.getUserAccountId())
                                    .projectId(result.getProjectId())
                                    .userName(MaskingUtil.getNameMask(AES256Util.decryptAES(properties.getKmsKey(), result.getName())))
                                    .snsId(AES256Util.decryptAES(properties.getKmsKey(), result.getSnsId()))
                                    .email(MaskingUtil.getEmailMask(AES256Util.decryptAES(properties.getKmsKey(), result.getContactEmail())))
                                    .phone(MaskingUtil.getPhoneMask(AES256Util.decryptAES(properties.getKmsKey(), result.getPhone())))
                                    .userType(result.getUserType())
                                    .build()));
                });
    }

    /**
     * ???????????? ???????????? ???????????? ????????????.
     * ????????????????????? ???????????? ????????? ??????.
     * @param keyword
     * @return
     */
    public Mono<List<UserInfoResponse>> findUserSearch(String keyword) {

        return userInfoDomainService.findAll()
                .filter(res -> AES256Util.decryptAES(properties.getKmsKey(), res.getEmail()).indexOf(keyword) != -1)
                .flatMap(user -> {
                    return Mono.just(
                            UserInfoResponse.builder().email(AES256Util.decryptAES(properties.getKmsKey(), user.getEmail()))
                                    .userAccountId(user.getId()).build()
                    );
                }).collectList();
    }
}
