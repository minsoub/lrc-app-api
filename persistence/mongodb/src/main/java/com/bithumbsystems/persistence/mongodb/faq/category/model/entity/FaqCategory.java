package com.bithumbsystems.persistence.mongodb.faq.category.model.entity;

import com.bithumbsystems.persistence.mongodb.faq.category.model.enums.LanguageType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("lrc_faq_category")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class FaqCategory {

    @MongoId(value = FieldType.STRING, targetType = FieldType.STRING)
    private String id;
    private String name;    //카테고리명
    private String order_no;       //노출순서

    private Boolean useYn;      //사용여부
    private Boolean delYn;      // 삭제여부
    private LanguageType language;    //언어

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id

}
