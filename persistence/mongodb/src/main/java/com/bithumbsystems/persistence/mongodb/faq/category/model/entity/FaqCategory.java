package com.bithumbsystems.persistence.mongodb.faq.category.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document
public class FaqCategory {

    @Id
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

    public FaqCategory(String order, String category, String code, Boolean useYn, String user, String language) {
        this.id = UUID.randomUUID();
        this.order = order;
        this.category = category;
        this.code = code;
        this.useYn = useYn;
        this.user = user;
        this.language = language;
        this.createDate = LocalDateTime.now();
    }
}
