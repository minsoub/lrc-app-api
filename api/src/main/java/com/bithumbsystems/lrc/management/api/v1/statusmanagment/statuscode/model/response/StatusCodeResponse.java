package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusCodeResponse {
    @Schema(description = "상태코드")
    private String id;

    @Schema(description = "상태명")
    private String name;        //상태명

    @Schema(description = "순서")
    private Integer orderNo;       //순서

    @Schema(description = "상위분류코드")
    private String parentCode;  //상위분류코드

    @Schema(description = "사용 여부")
    private Boolean useYn;      //사용 여부

    @Schema(description = "하위레벨 리스트")
    private List<StatusCodeResponse> children; //하위 레벨

    @Schema(description = "생성날짜")
    private LocalDateTime createDate; //생성날짜

    @Schema(description = "생성자 id")
    private String createAdminAccountId;  //생성자 id

    @Schema(description = "수정날짜")
    private LocalDateTime updateDate; //수정날짜

    @Schema(description = "수정자 id")
    private String updateAdminAccountId; //수정자 id
}
