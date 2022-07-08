package com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.response;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineMngResponse {

    @Schema(description = "계열관리 명")
    private String name;    //계열관리 명

    @Schema(description = "계열관리 코드")
    private String id;    //계열관리 코드

    @Schema(description = "계열타입")
    private LineType type;

    @Schema(description = "사용여부")
    private Boolean useYn;

    @Schema(description = "생성날짜")
    private LocalDateTime createDate; //생성날짜

    @Schema(description = "생성자 id")
    private String createAdminAccountId;  //생성자 id

    @Schema(description = "수정날짜")
    private LocalDateTime updateDate; //수정날짜

    @Schema(description = "수정자 id")
    private String updateAdminAccountId; //수정자 id
}
