package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "변경 히스토리")
public class HistoryResponse {

    private String id;

    @Schema(description = "메뉴")
    private String menu;            //메뉴

    @Schema(description = "항목")
    private String subject;         //항목

    @Schema(description = "작업 내역")
    private String taskHistory;     //작업내역

    @Schema(description = "수정자")
    private String customer;        //수정자

    @Schema(description = "수정날짜")
    private LocalDateTime updateDate; //수정날짜

    @Schema(description = "수정자 id")
    private String updateAdminAccountId; //수정자 id
}
