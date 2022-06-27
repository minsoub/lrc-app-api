package com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document(collection = "lrc_status_code")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class StatusCode {
    @MongoId(value = FieldType.STRING, targetType = FieldType.STRING)
    private String id;          //상태코드
    private String name;        //상태명
    private Integer orderNo;       //순서
    private String parentCode;  //상위분류코드
    private Boolean useYn;      //사용 여부

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id

}
