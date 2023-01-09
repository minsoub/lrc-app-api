package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Project info response.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "프로젝트 정보")
public class ProjectInfoResponse {

  private String id;

  @Schema(description = "프로젝트 코드")
  private String projectId;

  @Schema(description = "사업계열")
  private String businessCode;

  @Schema(description = "사업계열명")
  private String businessName;

  @Schema(description = "네트워크")
  private String networkCode;

  @Schema(description = "네트워크명")
  private String networkName;

  @Schema(description = "백서링크")
  private String whitepaperLink;

  @Schema(description = "컨트랙트 주소")
  private String contractAddress;

  @Schema(description = "최초발행일")
  private LocalDate createDate;

  @Schema(description = "상위 사업계열")
  private String parentBusinessCode;

  @Schema(description = "상위 사업계열명")
  private String parentBusinessName;

  @Schema(description = "상위 네트워크")
  private String parentNetworkCode;

  @Schema(description = "상위 네트워크명")
  private String parentNetworkName;
}
