package com.bithumbsystems.lrc.management.api.v1.faq.category.model.request;

import com.bithumbsystems.persistence.mongodb.faq.category.model.enums.LanguageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "FAQ 카테고리")
public class FaqCategoryRequest {

    @Schema(description = "카테고리ID")
    private String id;    //카테고리 ID

    @Schema(description = "카테고리명")
    private String name;    //카테고리명

    @Schema(description = "언어")
    private LanguageType language;    //언어

    @Schema(description = "노출순서")
    private String order_no;       //노출순서

    @Schema(description = "사용여부")
    private Boolean useYn;      //사용여부
}
