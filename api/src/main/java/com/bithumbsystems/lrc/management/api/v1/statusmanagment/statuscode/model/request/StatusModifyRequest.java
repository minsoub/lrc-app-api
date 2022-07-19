package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusModifyRequest {
    @Schema(description = "상태코드")
    private String id;        //상태코드

    @Schema(description = "상태명")
    private String name;        //상태명

    @Schema(description = "상태명(영문)")
    private String nameEn;

    @Schema(description = "순서")
    private Integer orderNo;       //순서

    @Schema(description = "상위분류코드")
    private String parentCode;  //상위분류코드

    @Schema(description = "사용 여부")
    private Boolean useYn;      //사용 여부
}
