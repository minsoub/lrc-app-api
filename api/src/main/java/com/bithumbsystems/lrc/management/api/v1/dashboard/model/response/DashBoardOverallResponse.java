package com.bithumbsystems.lrc.management.api.v1.dashboard.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/**
 * The type Dash board overall response.
 */
@Builder
@Getter
public class DashBoardOverallResponse {
  @Schema(description = "신규 사용자 건수 (24시간 이내 생성)")
  private long newUser24HourAgo;
  @Schema(description = "로그인 제한 사용자 건수")
  private long denyUserCnt;
  @Schema(description = "전체 사용자 건수")
  private long allUserCnt;
  @Schema(description = "사용자 신규 채팅 건수 - 24시간 이내 생성")
  private long newUserChat24HourAgo;
  @Schema(description = "Project ID's -신규 채팅 대상프로젝트, 콤마(,) 구분")
  private String projectIds;
}