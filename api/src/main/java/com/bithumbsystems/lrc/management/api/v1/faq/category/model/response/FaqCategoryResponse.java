package com.bithumbsystems.lrc.management.api.v1.faq.category.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FaqCategoryResponse {

    private UUID id;
    private String order;       //노출순서
    private String category;    //카테고리명
    private String code;        //카테고리 코드
    private Boolean useYn;      //사용여부
    private String user;        //등록자
    private String language;    //언어

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
}
