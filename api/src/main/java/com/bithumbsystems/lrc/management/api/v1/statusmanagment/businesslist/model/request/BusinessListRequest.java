package com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사업 계열")
public class BusinessListRequest {

    @Schema(description = "사업 계열 명")
    private String name;    //사업 계열 명

    @Schema(description = "사업 계열 코드")
    private String code;    //사업 계열 코드
}
