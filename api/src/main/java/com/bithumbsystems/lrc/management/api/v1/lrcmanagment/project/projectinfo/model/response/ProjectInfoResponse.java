package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "프로젝트 정보")
public class ProjectInfoResponse {
    
    private String id;

    @Schema(description = "프로젝트 코드")
    private String projectId;               //프로젝트 코드

    @Schema(description = "사업계열")
    private String businessCode;            //사업계열

    @Schema(description = "사업계열명")
    private String businessName;

    @Schema(description = "네트워크")
    private String networkCode;             //네트워크계열

    @Schema(description = "네트워크명")
    private String networkName;

    @Schema(description = "백서링크")
    private String whitepaperLink;          //백서링크

    @Schema(description = "컨트렉트 주소")
    private String contractAddress;         //컨트렉트 주소

    @Schema(description = "최초발행일")
    private LocalDate createDate;       //최초 발행일

  @Schema(description = "상위 사업계열")
  private String parentBusinessCode;            //사업계열

  @Schema(description = "상위 사업계열명")
  private String parentBusinessName;

  @Schema(description = "상위 네트워크")
  private String parentNetworkCode;             //네트워크계열

  @Schema(description = "상위 네트워크명")
  private String parentNetworkName;
}
