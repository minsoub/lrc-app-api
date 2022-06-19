package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusValueListRequest {

    @Schema(description = "상태명")
    private String name;        //상태명

    @Schema(description = "상태코드")
    private String code;        //상태코드

    @Schema(description = "트리 레벨")
    private String level;      //트리 레벨

    @Schema(description = "순서")
    private Integer order;       //순서

    @Schema(description = "분류코드")
    private String groupCode;   //분류코드

    @Schema(description = "상위분류코드")
    private String parentCode;  //상위분류코드

    @Schema(description = "사용 여부")
    private Boolean useYn;      //사용 여부
}
