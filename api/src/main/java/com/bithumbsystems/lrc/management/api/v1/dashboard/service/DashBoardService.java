package com.bithumbsystems.lrc.management.api.v1.dashboard.service;

import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardLineMngResponse;
import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardOverallResponse;
import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardStatus;
import com.bithumbsystems.lrc.management.api.v1.dashboard.model.response.DashBoardStatusCodeResponse;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.chat.service.ChatMessageDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.service.FoundationInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity.ProjectInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.service.ProjectInfoDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.model.enums.UserStatus;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.user.service.UserDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.service.LineMngDomainService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statuscode.service.StatusCodeDomainService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Dash board service.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DashBoardService {

  private final StatusCodeDomainService statusCodeDomainService;
  private final LineMngDomainService lineMngDomainService;
  private final FoundationInfoDomainService foundationInfoDomainService;
  private final ProjectInfoDomainService projectInfoDomainService;
  private final UserDomainService userDomainService;
  private final ChatMessageDomainService chatMessageDomainService;

  /**
   * 상태값 관리 모두 가져오기.
   *
   * @param searchFromDate the search from date
   * @param searchToDate   the search to date
   * @return DashBoardStatusCodeResponse status code
   */
  public Mono<List<DashBoardStatusCodeResponse>> getStatusCode(LocalDate searchFromDate, LocalDate searchToDate) {
    return statusCodeDomainService.findAllStatus()
        .map(statusCode ->
            DashBoardStatus.builder()
                .id(statusCode.getId())
                .name(statusCode.getName())
                .parentCode(statusCode.getParentCode())
                .orderNo(statusCode.getOrderNo())
                .useYn(statusCode.getUseYn())
                .build()
        )
        .filter(DashBoardStatus::getUseYn)
        .flatMap(c ->
            Mono.just(c)
                .zipWith(foundationInfoDomainService.findByCustomContractProcess(c.getId(), searchFromDate, searchToDate).count())
                .map(t -> {
                  t.getT1().setCount(t.getT2());
                  return t.getT1();
                })
        )
        .collectSortedList(Comparator.comparing(DashBoardStatus::getOrderNo))
        .map(c -> {
          List<DashBoardStatus> d = c.stream()
              .filter(f -> StringUtils.isEmpty(f.getParentCode()))
              .collect(Collectors.toList());

          return d.stream().map(s ->
              DashBoardStatusCodeResponse.builder()
                  .id(s.getId())
                  .name(s.getName())
                  .parentCode(s.getParentCode())
                  .orderNo(s.getOrderNo())
                  .useYn(s.getUseYn())
                  .count(s.getCount())
                  .children(
                      c.stream().filter(f -> s.getId().equals(f.getParentCode()))
                          .sorted(Comparator.comparing(DashBoardStatus::getOrderNo))
                          .collect(Collectors.toList())
                  )
                  .build()
          ).collect(Collectors.toList());
        });
  }

  /**
   * 계열관리 통계 모두 가져오기.
   *
   * @return DashBoardLineMngResponse line mng
   */
  public Mono<List<DashBoardLineMngResponse>> getLineMng() {
    Flux<ProjectInfo> projectInfo = projectInfoDomainService.findAll();
    Flux<DashBoardLineMngResponse> lineList = lineMngDomainService.findAllByUseYnIsTrue()
        .map(line -> DashBoardLineMngResponse.builder()
            .id(line.getId())
            .name(line.getName())
            .type(line.getType())
            .parentId(line.getParentId())
            .orderNo(line.getOrderNo())
            .useYn(line.getUseYn())
            .build()
        )
        .flatMap(line ->
            Mono.zip(Mono.just(line),
                    projectInfo.filter(project -> line.getId().equals(project.getBusinessCode())).count(),
                    projectInfo.filter(project -> line.getId().equals(project.getNetworkCode())).count())
                .map(tuple -> {
                      if (tuple.getT1().getType().equals(LineType.BUSINESS)) {
                        tuple.getT1().setCount(tuple.getT2());
                      } else if (tuple.getT1().getType().equals(LineType.NETWORK)) {
                        tuple.getT1().setCount(tuple.getT3());
                      }
                      return tuple.getT1();
                    }
                ))
        .sort(Comparator.comparing(DashBoardLineMngResponse::getType).thenComparing(DashBoardLineMngResponse::getOrderNo));

    return lineList.filter(line -> StringUtils.isEmpty(line.getParentId()))
        .concatMap(parent -> Mono.just(parent).zipWith(lineList.filter(line -> parent.getId().equals(line.getParentId())).collectList())
            .map(tuple -> {
              if (!tuple.getT2().isEmpty()) {
                tuple.getT1().setChildren(tuple.getT2());
              }
              return tuple.getT1();
            }))
        .collectList();
  }

  /**
   * Gets overall status.
   *
   * @return the overall status
   */
  public Mono<DashBoardOverallResponse> getOverallStatus() {
    LocalDateTime localDateTime = LocalDateTime.now().minusHours(24);
    Flux<UserInfo> userList = userDomainService.findList(null, null, null);
    Flux<ChatMessage> chatMessageList = chatMessageDomainService.findAllByRoleAndSiteIdAndIsDeleteFalse("USER", "62a15f4ae4129b518b133127")
        .filter(chat -> chat.getCreateDate().isAfter(localDateTime));

    return Mono.zip(userList.count(),
            userList.filter(user -> UserStatus.DENY_ACCESS.name().equals(user.getStatus())).count(),
            userList.filter(user -> ObjectUtils.defaultIfNull(user.getCreateDate(), LocalDateTime.of(1900, 1, 1, 0, 0))
                .isAfter(localDateTime)).count(),
            chatMessageList.count(),
            chatMessageList.map(ChatMessage::getChatRoom).collect(Collectors.joining(",")))
        .map(tuple -> DashBoardOverallResponse.builder()
            .allUserCnt(tuple.getT1())
            .denyUserCnt(tuple.getT2())
            .newUser24HourAgo(tuple.getT3())
            .newUserChat24HourAgo(tuple.getT4())
            .projectIds(tuple.getT5())
            .build()
        );
  }
}
