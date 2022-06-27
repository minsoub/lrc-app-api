package com.bithumbsystems.lrc.management.api.v1.faq.category.model.response;

import com.bithumbsystems.persistence.mongodb.faq.category.model.enums.LanguageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "FAQ 카테고리")
public class FaqCategoryResponse {

    private String id;
    @Schema(description = "노출순서")
    private String order_no;       //노출순서

    @Schema(description = "카테고리명")
    private String name;    //카테고리명

    @Schema(description = "사용여부")
    private Boolean useYn;      //사용여부

    @Schema(description = "언어")
    private LanguageType language;    //언어

    @Schema(description = "생성날짜")
    private LocalDateTime createDate; //생성날짜

    @Schema(description = "생성자 id")
    private String createAdminAccountId;  //생성자 id

    @Schema(description = "수정날짜")
    private LocalDateTime updateDate; //수정날짜

    @Schema(description = "수정자 id")
    private String updateAdminAccountId; //수정자 id
}
