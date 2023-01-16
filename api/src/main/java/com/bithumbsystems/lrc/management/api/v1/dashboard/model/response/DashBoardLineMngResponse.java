package com.bithumbsystems.lrc.management.api.v1.dashboard.model.response;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

/**
 * The type Dash board line mng response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashBoardLineMngResponse {

  @Schema(description = "사업 계열 코드")
  private String id;

  @Schema(description = "사업 계열 명")
  private String name;

  @Schema(description = "계열타입")
  private LineType type;

  @Schema(description = "사용여부")
  private Boolean useYn;

  @Schema(description = "상태 갯수")
  private Long count;

  @Schema(description = "정렬 순서")
  private Integer orderNo;

  @Schema(description = "상위 사업 계열 코드")
  private String parentId;

  @Schema(description = "하위 사업 계열 정보")
  private List<DashBoardLineMngResponse> children;

  /**
   * Gets order no.
   *
   * @return the order no
   */
  public Integer getOrderNo() {
    return ObjectUtils.defaultIfNull(orderNo, 999);
  }
}
