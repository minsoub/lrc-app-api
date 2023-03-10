package com.bithumbsystems.lrc.management.api.v1.faq.content.model.request;

import com.bithumbsystems.persistence.mongodb.faq.category.model.enums.LanguageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "FAQ 콘텐츠")
public class FaqContentRequest {

    @Schema(description = "사용자 id")
    private String userId;      //사용자 id

    @Schema(description = "노출순서")
    private Integer orderNo;       //노출순서

    @Schema(description = "카테고리 코드")
    private String categoryCode;    //카테고리 코드

    @Schema(description = "제목")
    private String title;       //제목

    @Schema(description = "내용")
    private String content;     //내용

    @Schema(description = "사용여부")
    private Boolean useYn;       //사용여부

//    @Schema(description = "등록자")
//    private String customer;    //등록자
//
//    @Schema(description = "등록자 이메일")
//    private String email;       //등록자 이메일

    @Schema(description = "언어")
    private LanguageType language;    //언어
}
