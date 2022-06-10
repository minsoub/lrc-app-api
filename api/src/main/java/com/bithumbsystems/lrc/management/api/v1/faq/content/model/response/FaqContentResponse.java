package com.bithumbsystems.lrc.management.api.v1.faq.content.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqContentResponse {

    private String id;
    private String userId;      //사용자 id
    private Integer order;       //노출순서
    private String category;    //카테고리
    private String categoryCode;    //카테고리 코드
    private String title;       //제목
    private String content;     //내용
    private Boolean useYn;       //사용여부
    private String customer;    //등록자
    private String email;       //등록자 이메일
    private String language;    //언어

    private LocalDateTime createDate; //생성날짜D
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id

}
