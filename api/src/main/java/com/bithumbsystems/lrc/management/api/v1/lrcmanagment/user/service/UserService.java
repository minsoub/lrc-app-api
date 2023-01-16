package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.service;

import static java.util.stream.Collectors.joining;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.mapper.FoundationInfoMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.response.FoundationInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.mapper.UserAccountMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response.UserAccountResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.mapper.UserMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.user.model.response.UserResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserAccountDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.model.enums.UserStatus;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.service.UserDomainService;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * The type User service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final AwsProperties awsProperties;
  private final UserDomainService userDomainService;
  private final FoundationInfoDomainService foundationInfoDomainService;
  private final UserAccountDomainService userAccountDomainService;

  /**
   * Gets list.
   *
   * @param searchFromDate the search from date
   * @param searchToDate   the search to date
   * @param userStatus     the user status
   * @param keyword        the keyword
   * @return the list
   */
  public Mono<List<UserResponse>> getList(LocalDate searchFromDate, LocalDate searchToDate, UserStatus userStatus, String keyword) {
    return userDomainService.findList(searchFromDate, searchToDate, userStatus)
        .map(UserMapper.INSTANCE::userInfoTouserResponse)
        .map(user -> {
          user.setEmail(AES256Util.decryptAES(awsProperties.getKmsKey(), user.getEmail()));
          user.setStatusName(Enum.valueOf(UserStatus.class, user.getStatus()).getCodeName());
          return user;
        })
        .flatMap(user -> Mono.zip(Mono.just(user),
                    foundationInfoDomainService.findByCreateAccountId(user.getId())
                        .map(res -> {
                          FoundationInfoResponse foundationInfoResponse = FoundationInfoMapper.INSTANCE.foundationInfoResponse(res);
                          foundationInfoResponse.setProjectName(res.getName());
                          return foundationInfoResponse;
                        })
                        .collectList(),
                    userAccountDomainService.findByCustomProjectInfo(
                            AES256Util.encryptAES(awsProperties.getKmsKey(), user.getEmail(), awsProperties.getSaltKey(), awsProperties.getIvKey()))
                        .map(res -> {
                          UserAccountResponse userAccountResponse = UserAccountMapper.INSTANCE.userAccountResponse(res);
                          if (res.getFoundationInfo() != null) {
                            userAccountResponse.setProjectName(res.getFoundationInfo().getName());
                          }
                          return userAccountResponse;
                        })
                        .collectList()
                )
                .map(tuple -> {
                  tuple.getT1().setCreateProjectsName(
                      tuple.getT2().stream().map(FoundationInfoResponse::getProjectName).collect(joining(", ")));
                  tuple.getT1().setJoinProjectsName(
                      tuple.getT3().stream().map(UserAccountResponse::getProjectName).collect(joining(", ")));
                  return tuple.getT1();
                })
                .filter(t1 -> StringUtils.isEmpty(keyword) || t1.getCreateProjectsName().toUpperCase().contains(keyword.toUpperCase()) || t1.getJoinProjectsName().toUpperCase().contains(keyword.toUpperCase()) || t1.getEmail().toUpperCase().contains(keyword.toUpperCase()))
        )
        .collectSortedList(Comparator.comparing(UserResponse::getCreateDate).reversed());
  }
}
