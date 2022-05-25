package com.bithumbsystems.lrc.management.api.v1.faq.category.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqCategoryRequest {

    private String order;       //노출순서
    private String category;    //카테고리명
    private String code;        //카테고리 코드
    private Boolean useYn;      //사용여부
    private String user;        //등록자
    private String language;    //언어
}
