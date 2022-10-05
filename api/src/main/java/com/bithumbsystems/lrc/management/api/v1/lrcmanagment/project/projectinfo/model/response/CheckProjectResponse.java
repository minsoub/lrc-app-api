package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "프로젝트 체크")
public class CheckProjectResponse {
    private Boolean result;
}
