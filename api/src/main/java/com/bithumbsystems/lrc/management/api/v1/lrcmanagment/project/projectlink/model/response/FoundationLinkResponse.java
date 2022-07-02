package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "재단 정보 링크")
public class FoundationLinkResponse {

    private String id;

    @Schema(description = "프로젝트 id")
    private String projectId;           //프로젝트 id

    @Schema(description = "프로젝트명")
    private String projectName;

    @Schema(description = "심볼")
    private String symbol;
}
