package com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.request;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "계열 관리")
public class LineMngRequest {

    @Schema(description = "계열관리 코드")
    private String id;

    @Schema(description = "계열관리 명")
    private String name;    //계열관리 명

    @Schema(description = "계열관리 타입")
    private LineType type;    //계열관리 타입

    @Schema(description = "사용여부")
    private Boolean useYn;
}
