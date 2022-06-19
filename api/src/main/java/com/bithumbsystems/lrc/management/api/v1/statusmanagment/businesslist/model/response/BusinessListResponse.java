package com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessListResponse {

    private String id;

    @Schema(description = "사업 계열 명")
    private String name;    //사업 계열 명

    @Schema(description = "사업 계열 코드")
    private String code;    //사업 계열 코드

    @Schema(description = "생성날짜")
    private LocalDateTime createDate; //생성날짜

    @Schema(description = "생성자 id")
    private String createAdminAccountId;  //생성자 id

    @Schema(description = "수정날짜")
    private LocalDateTime updateDate; //수정날짜

    @Schema(description = "수정자 id")
    private String updateAdminAccountId; //수정자 id
}
