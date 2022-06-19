package com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "네트워크 계열")
public class NetworkListRequest {

    @Schema(description = "네트워크 계열 명")
    private String name;    //네트워크 계열 명

    @Schema(description = "네트워크 계열 코드")
    private String code;    //네트워크 계열 코드
}
