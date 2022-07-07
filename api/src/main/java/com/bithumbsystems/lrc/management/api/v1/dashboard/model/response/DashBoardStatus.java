package com.bithumbsystems.lrc.management.api.v1.dashboard.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashBoardStatus {

    @Schema(description = "상태코드")
    private String id;

    @Schema(description = "상태명")
    private String name;

    @Schema(description = "상위분류코드")
    private String parentCode;

    @Schema(description = "순서")
    private Integer orderNo;

    @Schema(description = "사용 여부")
    private Boolean useYn;

    @Schema(description = "상태 갯수")
    private Long count;
}
