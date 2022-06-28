package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "프로젝트 정보")
public class ProjectInfoRequest {

    @Schema(description = "프로젝트 코드")
    private String projectId;               //프로젝트 코드

    @Schema(description = "사업계열")
    private String businessLine;            //사업계열

    @Schema(description = "네트워크")
    private String networkLine;             //네트워크계열

    @Schema(description = "백서링크")
    private String whitepaperLink;          //백서링크

    @Schema(description = "컨트렉트 주소")
    private String contractAddress;         //컨트렉트 주소

    @Schema(description = "최초발행일")
    private LocalDateTime createDate;       //최초 발행일
}
