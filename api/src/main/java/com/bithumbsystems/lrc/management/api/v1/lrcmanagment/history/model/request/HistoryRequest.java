package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "변경 히스토리")
public class HistoryRequest {

    @Schema(description = "메뉴")
    private String menu;            //메뉴

    @Schema(description = "항목")
    private String subject;         //항목

    @Schema(description = "작업 내역")
    private String taskHistory;     //작업내역

    @Schema(description = "수정자")
    private String customer;        //수정자
}
