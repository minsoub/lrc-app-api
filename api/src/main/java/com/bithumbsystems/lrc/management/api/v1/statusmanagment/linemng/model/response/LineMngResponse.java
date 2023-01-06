package com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.response;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Line mng response.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineMngResponse {

  @Schema(description = "계열관리 명")
  private String name;

  @Schema(description = "계열관리 코드")
  private String id;

  @Schema(description = "계열타입", allowableValues = {"BUSINESS", "NETWORK"})
  private LineType type;

  @Schema(description = "사용여부")
  private Boolean useYn;

  @Schema(description = "생성날짜 (yyyy-MM-dd hh:mm)")
  private LocalDateTime createDate;

  @Schema(description = "생성자 id")
  private String createAdminAccountId;

  @Schema(description = "수정날짜 (yyyy-MM-dd hh:mm)")
  private LocalDateTime updateDate;

  @Schema(description = "수정자 id")
  private String updateAdminAccountId;

  @Schema(description = "순서")
  private Integer orderNo;

  @Schema(description = "상위분류코드")
  private String parentId;

  @Schema(description = "하위분류 리스트", implementation = LineMngResponse.class)
  private List<LineMngResponse> children;

  /**
   * Sets children.
   *
   * @param children the children
   * @return the children
   */
  public LineMngResponse setChildren(List<LineMngResponse> children) {
    this.children = children;
    return this;
  }

  /**
   * Gets order no.
   *
   * @return the order no
   */
  public Integer getOrderNo() {
    return orderNo == null ? 999 : orderNo;
  }
}
