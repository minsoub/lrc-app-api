package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "프로젝트 링크")
public class ProjectLinkResponse {

    private String id;

    @Schema(description = "프로젝트 id")
    private String projectId;           //프로젝트 id

    @Schema(description = "심볼")
    private String symbol;              //심볼

    @Schema(description = "링크 프로젝트 id")
    private String linkProjectId;       //링크 프로젝트 id

    @Schema(description = "링크 프로젝트 심볼")
    private String linkProjectSymbol;   //링크 프로젝트 심볼
}
