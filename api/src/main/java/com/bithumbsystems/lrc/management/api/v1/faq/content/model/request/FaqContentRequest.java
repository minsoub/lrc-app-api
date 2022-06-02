package com.bithumbsystems.lrc.management.api.v1.faq.content.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqContentRequest {
    private String userId;      //사용자 id
    private String order;       //노출순서
    private String categoryCode;    //카테고리
    private String title;       //제목
    private String content;     //내용
    private Boolean useYn;       //사용여부
    private String customer;    //등록자
    private String email;       //등록자 이메일
    private String language;    //언어
}
