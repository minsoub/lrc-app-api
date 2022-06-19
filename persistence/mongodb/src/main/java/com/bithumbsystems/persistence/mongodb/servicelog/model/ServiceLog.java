package com.bithumbsystems.persistence.mongodb.servicelog.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document("lrc_service_log")
public class ServiceLog {

    @Id
    private String id;          //uuid;
    private String seq;         //SN
    private String account;     //user id;
    private String ip;          //사용자 ip
    private String menuId;      //메뉴 id
    private String menuName;    //메뉴명
    private String crud;        //crud
    private String uri;         //url
    private String parameter;   //파라미터
    private String referer;
    private String device;      //장비
    private String result;      //성공/실패 여부
    private String message;     //메시지 정보
    private LocalDateTime createDdate;   //발행일시

    public ServiceLog(String seq, String account, String ip, String menuId, String menuName,String crud,
                      String uri, String parameter, String referer, String device, String result, String message) {
        this.id = UUID.randomUUID().toString();
        this.seq = seq;
        this.account = account;
        this.ip = ip;
        this.menuId = menuId;
        this.menuName = menuName;
        this.crud = crud;
        this.uri = uri;
        this.parameter = parameter;
        this.referer = referer;
        this.device = device;
        this.result = result;
        this.message = message;
        this.createDdate = LocalDateTime.now();

    }
}
