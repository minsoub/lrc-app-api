package com.bithumbsystems.persistence.mongodb.faq.content.model.entity;

import com.bithumbsystems.persistence.mongodb.faq.category.model.enums.LanguageType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("lrc_faq_content")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class FaqContent {

    @Id
    private String id;
    private String userId;      //사용자 id
    private Integer order;       //노출순서
    private String categoryCode;    //카테고리 코드
    private String title;       //제목
    private String content;     //내용
    private Boolean useYn;       //사용여부
    private String customer;    //등록자
    private String email;       //등록자 이메일
    private LanguageType language;    //언어

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id

}
