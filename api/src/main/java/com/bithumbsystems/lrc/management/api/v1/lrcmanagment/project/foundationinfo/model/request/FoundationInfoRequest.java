package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "재단 정보")
public class FoundationInfoRequest {

    @Schema(description = "id")
    private String id;

    @Schema(description = "프로젝트 id")
    private String projectId;       //프로젝트 id

    @Schema(description = "프로젝트 명")
    private String projectName;     //프로젝트 명

    @Schema(description = "심볼")
    private String symbol;          //심볼

    @Schema(description = "계약상태 code")
    private String contractCode;    //계약상태 code

    @Schema(description = "진행상태 code")
    private String progressCode;    //진행상태 code

    @Schema(description = "관리자 메모")
    private String adminMemo;       //관리자 메모
}