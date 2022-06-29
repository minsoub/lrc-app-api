package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.response.FoundationInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.response.IcoInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.response.MarketingQuantityResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response.ProjectInfoResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.response.ProjectLinkResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FoundationResponse {

    private String id;

    @Schema(description = "프로젝트 id")
    private String projectId;      //프로젝트 id

    @Schema(description = "프로젝트 명")
    private String projectName;     //프로젝트명

    @Schema(description = "심볼")
    private String symbol;          //심볼

    @Schema(description = "계약상태 code")
    private String contractCode;    //계약상태 code

    @Schema(description = "계약상태")
    private String contractName;    //계약상태

    @Schema(description = "진행상태 code")
    private String progressCode;    //진행상태 code

    @Schema(description = "진행상태")
    private String progressName;    //진행상태

    @Schema(description = "사업계열")
    private String businessCode;    //사업계열 code

    @Schema(description = "사업계열")
    private String businessName;    //사업계열

    @Schema(description = "네트워크계열")
    private String networkCode;     //네트워크계열 code

    @Schema(description = "네트워크계열")
    private String networkName;     //네트워크계열

    @Schema(description = "마케팅 수량 최소")
    private Long minimumQuantity;    //마케팅 수량 최소

    @Schema(description = "마케팅 수량 실제")
    private Long actualQuantity;   //마케팅 수량 실제

    @Schema(description = "연결프로젝트")
    private String projectLink;     //연결프로젝트

    @Schema(description = "상장일")
    private LocalDateTime ipoDate;         //상장일

    @Schema(description = "생성날짜")
    private LocalDateTime createDate; //생성날짜

    @Schema(description = "생성자 id")
    private String createAdminAccountId;  //생성자 id

    private List<FoundationInfoResponse> foundationInfoResponses;
    private List<ProjectInfoResponse> projectInfos;
    private List<IcoInfoResponse> icoInfos;
    private List<MarketingQuantityResponse> marketingQuantities;
    private List<ProjectLinkResponse> projectLinks;



}
