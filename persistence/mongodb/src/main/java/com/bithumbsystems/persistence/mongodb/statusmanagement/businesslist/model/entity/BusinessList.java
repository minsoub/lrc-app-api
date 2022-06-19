package com.bithumbsystems.persistence.mongodb.statusmanagement.businesslist.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document("lrc_business_list")
public class BusinessList {

    @Id
    private String id;
    private String name;        //사업계열명
    private String code;        //사업계열 코드

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id

    public BusinessList(String name, String code) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.code = code;
        this.createDate = LocalDateTime.now();
    }

}
