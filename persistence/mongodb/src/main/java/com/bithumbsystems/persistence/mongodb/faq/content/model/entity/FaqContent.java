package com.bithumbsystems.persistence.mongodb.faq.content.model.entity;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document
public class FaqContent {

    @Id
    private UUID id;
    private String userId;      //사용자 id
    private String order;       //노출순서
    private String category;    //카테고리
    private String title;       //제목
    private String content;     //내용
    private Boolean useYn;       //사용여부
    private String customer;    //등록자
    private String language;    //언어

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id

    public FaqContent(String userId, String order, String category, String title, String content, Boolean useYn, String customer, String language) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.order = order;
        this.category = category;
        this.title = title;
        this.content = content;
        this.useYn = useYn;
        this.customer = customer;
        this.language = language;
        this.createDate = LocalDateTime.now();
    }
}
