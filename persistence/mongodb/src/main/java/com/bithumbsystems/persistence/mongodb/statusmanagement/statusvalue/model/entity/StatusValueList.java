package com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document("lrc_status_value_list")
public class StatusValueList {

    @Transient
    public static final String SEQUENCE_NAME = "status_value_sequence";

    @Id
    private String id;
    private String name;        //상태명
    private String code;        //상태코드
    private String level;       //트리 레벨
    private Integer order;       //순서
    private String groupCode;   //분류코드
    private String parentCode;  //상위분류코드
    private Boolean useYn;      //사용 여부

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id

    public StatusValueList(String name, String code, String level, Integer order, String groupCode, String parentCode, Boolean useYn) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.code = code;
        this.level = level;
        this.order = order;
        this.groupCode = groupCode;
        this.parentCode = parentCode;
        this.useYn = useYn;
        this.createDate = LocalDateTime.now();
    }
}
