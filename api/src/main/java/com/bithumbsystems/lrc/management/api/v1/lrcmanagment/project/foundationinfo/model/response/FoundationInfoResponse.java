package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "재단 정보")
public class FoundationInfoResponse {

    @Schema(description = "id")
    private String id;

    @Schema(description = "프로젝트 명")
    private String projectName;     //프로젝트 명

    @Schema(description = "심볼")
    private String symbol;          //심볼

    @Schema(description = "계약상태 code")
    private String contractCode;    //계약상태 code

    @Schema(description = "계약상태 명칭")
    private String contractName;

    @Schema(description = "진행상태 code")
    private String processCode;    //진행상태 code

    @Schema(description = "진행상태명")
    private String processName;

    @Schema(description = "관리자 메모")
    private String adminMemo;       //관리자 메모
}