package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.service;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryLog;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.mapper.UserAccountMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserAccountRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request.UserSaveRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response.UserAccountResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response.UserInfoResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserAccount;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserAccountDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserInfoDomainService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    private final FoundationInfoDomainService foundationInfoDomainService;

    private final AwsProperties properties;
    private final HistoryLog historyLog;

    /**
     *  프로젝트 아이디로 사용자 라스트 찾기
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
     * 생성자 정보를 리턴한다.
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
     * 담당자 정보 신규 저장
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
                        historyLog.send(projectId, "프로젝트 관리>담당자 정보", "담당자", "담당자 추가", AES256Util.decryptAES( properties.getCryptoKey(), result.getEmail()), account);
                        return Mono.just(UserAccountMapper.INSTANCE.userAccountResponse(res));
                    });
                });
    }

    /**
     * 담당자 정보를 저장한다.
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
                                            historyLog.send(projectId, "프로젝트 관리>담당자 정보", "이름", "수정", name, account);
                                            historyLog.send(projectId, "프로젝트 관리>담당자 정보", "연락처", "수정", phone, account);
                                            historyLog.send(projectId, "프로젝트 관리>담당자 정보", "SNS ID", "수정", sns_id, account);
                                            historyLog.send(projectId, "프로젝트 관리>담당자 정보", "이메일 주소", "수정", email, account);

                                            return Mono.just(UserAccountMapper.INSTANCE.userAccountResponse(r));
                                        });
                            });
                })
                .collectList();
    }

    /**
     * 해당 프로젝트에서 담당자 탈퇴 처리
     * @param projectId
     * @param userId
     * @param account
     * @return
     */
    public Mono<UserAccountResponse> deleteUser(String projectId, String userId, Account account) {
        return userAccountDomainService.findById(userId)
                .flatMap(result -> {
                    historyLog.send(projectId, "프로젝트 관리>담당자 정보", "담당자", "담당자 탈퇴", AES256Util.decryptAES( properties.getCryptoKey(), result.getName()), account);
                    return userAccountDomainService.delete(result)
                            .then(Mono.just(UserAccountMapper.INSTANCE.userAccountResponse(result)));
                });
    }

    /**
     * 키워드로 거래지원 사용자를 조회한다.
     *
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
